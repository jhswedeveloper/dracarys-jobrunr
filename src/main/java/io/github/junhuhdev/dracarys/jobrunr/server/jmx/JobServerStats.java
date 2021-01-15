package io.github.junhuhdev.dracarys.jobrunr.server.jmx;

import javax.management.JMException;
import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static io.github.junhuhdev.dracarys.jobrunr.utils.reflection.ReflectionUtils.cast;

public class JobServerStats {

    private final OperatingSystemMXBean operatingSystemMXBean;
    private final MBeanServer platformMBeanServer;
    private final ConcurrentMap<String, Object> cachedValues = new ConcurrentHashMap<>();

    public JobServerStats() {
        this(ManagementFactory.getOperatingSystemMXBean(), ManagementFactory.getPlatformMBeanServer());
    }

    protected JobServerStats(OperatingSystemMXBean operatingSystemMXBean, MBeanServer platformMBeanServer) {
        this.operatingSystemMXBean = operatingSystemMXBean;
        this.platformMBeanServer = platformMBeanServer;
    }

    public Long getProcessMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public Long getProcessFreeMemory() {
        return Runtime.getRuntime().maxMemory() - (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    }

    public Long getProcessAllocatedMemory() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
    }

    public Long getSystemTotalMemory() {
        return getMXBeanValueAsLong("TotalPhysicalMemorySize");
    }

    public Long getSystemFreeMemory() {
        return getMXBeanValueAsLong("FreePhysicalMemorySize");
    }

    public Double getSystemCpuLoad() {
        return getMXBeanValueAsDouble("SystemCpuLoad");
    }

    public Double getProcessCpuLoad() {
        return getMXBeanValueAsDouble("ProcessCpuLoad");
    }

    private Double getMXBeanValueAsDouble(String name) {
        double value = ((Number) getMXBeanValue(name)).doubleValue();
        if (!Double.isNaN(value)) {
            cachedValues.put(name, value);
        }
        return cast(cachedValues.getOrDefault(name, -1.0));
    }

    private Long getMXBeanValueAsLong(String name) {
        long value = ((Number) getMXBeanValue(name)).longValue();
        if (value > 0) {
            cachedValues.put(name, value);
        }
        return cast(cachedValues.getOrDefault(name, -1L));
    }

    // visible for testing
    // see bug JDK-8193878
    <O> O getMXBeanValue(String name) {
        try {
            final Object attribute = platformMBeanServer.getAttribute(operatingSystemMXBean.getObjectName(), name);
            return cast(attribute);
        } catch (JMException | NullPointerException ex) {
            return cast(-1);
        }
    }
}
