package com.hk.json;

public interface JsonAdapter<T>
{
	public JsonObject serialize(T obj);

	public T fromJsonObject(JsonObject json);
}
