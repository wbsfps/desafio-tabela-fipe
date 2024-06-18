package br.com.tabelafipe.fipe.service;

import java.util.List;

public interface IConvertingData {
    <T> T getData(String json, Class<T> tClass);

    <T> List<T> getList(String json, Class<T> tclass);
}
