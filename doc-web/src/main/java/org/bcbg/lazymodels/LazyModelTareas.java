/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.lazymodels;

import org.bcbg.util.IrisUtil;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import org.bcbg.util.Utils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author jesus
 * @param <T>
 */
public class LazyModelTareas<T extends Object> extends LazyDataModel<T> {

    private Class entitiArray;
    private Class entiti;
    private Map<String, FilterMeta> filterss;
    private Map<String, SortMeta> orderBy;
    private final RestTemplate restTemplate;
    private final String url;
    private String urlFilter;
    private HttpEntity<String> entity;
    private List<T> result = null;

    public LazyModelTareas(String url, String token) {
        this.restTemplate = new RestTemplate();
        this.entity = IrisUtil.headers(token);
        this.url = url;
    }

    public LazyModelTareas(String url, Class entitiArray, String token) {
        this.restTemplate = new RestTemplate();
        this.entity = IrisUtil.headers(token);
        this.url = url;
        this.entitiArray = entitiArray;
    }

    public LazyModelTareas(String url, Class entitiArray, Map<String, FilterMeta> filterss, String token) {
        this.entitiArray = entitiArray;
        this.filterss = filterss;
        this.restTemplate = new RestTemplate();
        this.entity = IrisUtil.headers(token);
        this.url = url;
    }

    public LazyModelTareas(String url, Class entitiArray, Integer idUser, String token) {
        this.entitiArray = entitiArray;
        this.restTemplate = new RestTemplate();
        this.entity = IrisUtil.headers(token);
        this.url = url;
    }

    @Override
    public List<T> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        result = null;
        String tempUrl = null;
        try {
            this.setRowCount(0);
            this.setPageSize(pageSize);
            String predicate = "", order = "";
            predicate += formatFilter(filterBy);
            predicate += formatFilter(getFilterss());
            order = formatOrder(orderBy);
            if (!url.contains("?search=")) {
                urlFilter = url + "?search=" + predicate + order;
                tempUrl = String.format("%spage=%d&size=%d", urlFilter, this.getCurrentPage(first), pageSize);
            } else if (url.contains("searchUrl?search=(assignee:")) {
                tempUrl = url.replace("search=(assignee:", "page=" + this.getCurrentPage(first) + "&size=" + pageSize + "&search=(assignee:") + (predicate.isEmpty() ? "" : predicate + order);
                //tempUrl = String.format("%spage=%d&size=%d", urlFilter, this.getCurrentPage(first), pageSize);
                //urlFilter = url + " AND " + predicate + order;
            } else {
                urlFilter = url + " AND " + predicate + order;
                tempUrl = String.format("%spage=%d&size=%d", urlFilter, this.getCurrentPage(first), pageSize);
            }

            System.out.println("//Url tempUrl " + tempUrl);
            //URI uri = new URI(tempUrl);
            ResponseEntity<Object[]> responseEntity = restTemplate.exchange(tempUrl, HttpMethod.GET, entity, this.entitiArray == null ? Object[].class : this.entitiArray);
            //ResponseEntity<Object[]> responseEntity = restTemplate.getForEntity(tempUrl, this.entitiArray == null ? Object[].class : this.entitiArray);
            Object[] forObject = responseEntity.getBody();
            if (forObject != null) {
                HttpHeaders headers = responseEntity.getHeaders();
                List<String> listSize = headers.get("rootSize");
                if (listSize != null) {
                    this.setRowCount(Integer.valueOf(listSize.get(0)));
                    //System.out.println(Integer.valueOf(listSize.get(0)));
                }
                result = Arrays.asList((T[]) forObject);
            }
        } catch (RestClientException ex) {
            Logger.getLogger(LazyModelWS.class.getName()).log(Level.SEVERE, tempUrl, ex);
            return result;
        }
        return result;
    }

    protected String formatFilter(Map<String, FilterMeta> filters) {
        String predicate = "";
        SimpleDateFormat f = new SimpleDateFormat("MM/dd/yyyy");
        for (FilterMeta meta : filters.values()) {
            String filterField = meta.getField();
            Object filterValue = meta.getFilterValue();

            if (filterValue != null && !filterValue.toString().isEmpty()) {
                if (filterValue instanceof Date) {
                    predicate += " AND " + filterField + ":'*" + f.format(filterValue) + "*'";
                } else {
                    predicate += " AND " + filterField + ":'*" + filterValue.toString().toLowerCase() + "*'";
                }
            }
        }
        return predicate;
    }

    protected String formatOrder(Map<String, SortMeta> orders) {
        String orderby = "";
        if (orders != null) {
            for (Map.Entry<String, SortMeta> entry : orders.entrySet()) {
                if (entry.getValue() != null && !entry.getValue().toString().isEmpty()) {
                    orderby += String.format("sort=%s,%s&", entry.getKey(), entry.getValue());
                }
            }
        }
        return orderby;
    }

    @Override
    public T getRowData(String rowKey) {
        T ob = null;
        Object x = rowKey;
        try {
            Object[] forObject = (Object[]) restTemplate.getForObject(url + "?id=" + rowKey, this.entitiArray == null ? Object[].class : this.entitiArray);
            if (forObject != null) {
                ob = (T) forObject[0];
            }
        } catch (RestClientException e) {
            Logger.getLogger(LazyModelWS.class.getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

    private Integer getCurrentPage(int first) {
        return (first / this.getPageSize());
    }

    public Class getEntitiArray() {
        return entitiArray;
    }

    public void setEntitiArray(Class entitiArray) {
        this.entitiArray = entitiArray;
    }

    public Class getEntiti() {
        return entiti;
    }

    public void setEntiti(Class entiti) {
        this.entiti = entiti;
    }

    public Map<String, FilterMeta> getFilterss() {
        if (filterss == null) {
            filterss = new HashMap<>();
        }
        return filterss;
    }

    public void setFilterss(Map<String, FilterMeta> filterss) {
        this.filterss = filterss;
    }

    public String getUrlFilter() {
        return urlFilter;
    }

    public void setUrlFilter(String urlFilter) {
        this.urlFilter = urlFilter;
    }

    public String getUrlReport() {
        if (filterss != null) {
            String predicate = "";
            predicate += formatFilter(filterss) + formatOrder(orderBy);
            return urlFilter = url + "?" + predicate;
        }
        return null;
    }

    public Map<String, SortMeta> getOrderBy() {
        if (orderBy == null) {
            orderBy = new HashMap<>();
        }
        return orderBy;
    }

    public void setOrderBy(Map<String, SortMeta> orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        if (result == null) {
            return 0;
        }
        return (int) result.stream()
                .filter(o -> Utils.filter(FacesContext.getCurrentInstance(), filterBy.values(), o))
                .count();
    }
}
