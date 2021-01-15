package io.github.junhuhdev.dracarys.jobrunr.utils.mapper.jsonb;

import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

public class JobRunrJsonb implements Jsonb {

    private final Jsonb delegate;

    public JobRunrJsonb(Jsonb delegate) {
        this.delegate = delegate;
    }

    @Override
    public void close() throws Exception {
        delegate.close();
    }

    @Override
    public <T> T fromJson(String s, Class<T> aClass) {
        return delegate.fromJson(s, aClass);
    }

    @Override
    public <T> T fromJson(String s, Type type) {
        return delegate.fromJson(s, type);
    }

    @Override
    public <T> T fromJson(Reader reader, Class<T> aClass) {
        return delegate.fromJson(reader, aClass);
    }

    @Override
    public <T> T fromJson(Reader reader, Type type) {
        return delegate.fromJson(reader, type);
    }

    @Override
    public <T> T fromJson(InputStream inputStream, Class<T> aClass) {
        return delegate.fromJson(inputStream, aClass);
    }

    @Override
    public <T> T fromJson(InputStream inputStream, Type type) {
        return delegate.fromJson(inputStream, type);
    }

    @Override
    public String toJson(Object o) {
        return delegate.toJson(o);
    }

    @Override
    public String toJson(Object o, Type type) {
        return delegate.toJson(o, type);
    }

    @Override
    public void toJson(Object o, Writer writer) {
        delegate.toJson(o, writer);
    }

    @Override
    public void toJson(Object o, Type type, Writer writer) {
        delegate.toJson(o, type, writer);
    }

    @Override
    public void toJson(Object o, OutputStream outputStream) {
        delegate.toJson(o, outputStream);
    }

    @Override
    public void toJson(Object o, Type type, OutputStream outputStream) {
        delegate.toJson(o, type, outputStream);
    }

    public JsonValue fromJsonToJsonValue(Object object) {
        return delegate.fromJson(delegate.toJson(object), JsonValue.class);
    }

    public <T> T fromJsonValue(JsonValue jsonValue, Class<T> clazz) {
        return delegate.fromJson(jsonValue.toString(), clazz);
    }
}
