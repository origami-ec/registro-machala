/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.lazymodels;

import java.beans.IntrospectionException;
import org.bcbg.entities.User;
import org.bcbg.util.IrisUtil;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
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
import org.primefaces.model.filter.FilterConstraint;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author gutya
 * @param <T>
 */
public class LazyModelWS<T extends Object> extends LazyDataModel<T> {

    private Class entitiArray;
    private Class entiti;
    private Map<String, FilterMeta> filterss;
    private Map<String, SortMeta> orderBy;
    private final RestTemplate restTemplate;
    private final String url;
    private String urlFilter;
    private List<T> result;
    private ResponseEntity<Object[]> responseEntity;

    private HttpEntity<String> entity;

    public LazyModelWS(String url, String token) {
        this.restTemplate = new RestTemplate();
        this.entity = IrisUtil.headers(token);
        this.url = url;
    }

    public LazyModelWS(String url, Class entitiArray, String token) {
        this.restTemplate = new RestTemplate();
        this.entity = IrisUtil.headers(token);
        this.url = url;
        this.entitiArray = entitiArray;
    }

    public LazyModelWS(String url, Class entitiArray, Map<String, FilterMeta> filterss, String token) {
        this.entitiArray = entitiArray;
        this.entity = IrisUtil.headers(token);
        this.filterss = filterss;
        this.restTemplate = new RestTemplate();
        this.url = url;
    }

    public LazyModelWS(String url, Class entitiArray, Integer idUser, String token) {
        this.entitiArray = entitiArray;
        this.entity = IrisUtil.headers(token);
        this.restTemplate = new RestTemplate();
        this.url = url;
    }

    @Override
    public List<T> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        System.out.println("load");
        result = null;
        String tempUrl = null;
        try {
            System.out.println("first " + first + " pageSize " + pageSize);
            this.setRowCount(0);
            this.setPageSize(pageSize);
            String predicate = "", order = "";
            predicate += formatFilter(filterBy);
            predicate += formatFilter(getFilterss());
            order = formatOrder(orderBy);
            if (!url.contains("?")) {
                urlFilter = url + "?" + predicate + order;
            } else {
                //char lastUrl = url.charAt(url.length() - 1);
                urlFilter = url + "&" + predicate + order;
            }

            tempUrl = String.format("%spage=%d&size=%d", urlFilter, this.getCurrentPage(first), pageSize);
            System.out.println("//Url tempUrl " + tempUrl);
            //ResponseEntity<Object[]> responseEntity = restTemplate.getForEntity(tempUrl, this.entitiArray == null ? Object[].class : this.entitiArray);
            responseEntity = restTemplate.exchange(tempUrl, HttpMethod.GET, entity, this.entitiArray == null ? Object[].class : this.entitiArray);
            Object[] forObject = responseEntity.getBody();
            if (forObject != null) {
                HttpHeaders headers = responseEntity.getHeaders();
                List<String> listSize = headers.get("rootSize");
                if (listSize != null) {
                    this.setRowCount(Integer.valueOf(listSize.get(0)));
                }
                result = Arrays.asList((T[]) forObject);
                System.out.println("result: " + result.size());
            }
        } catch (RestClientException ex) {
            Logger.getLogger(LazyModelWS.class.getName()).log(Level.SEVERE, tempUrl, ex);
            return result;
        }
        return result;
    }

    protected String formatFilter(Map<String, FilterMeta> filters) {
        String predicate = "";
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        for (FilterMeta meta : filters.values()) {
            String filterField = meta.getField();
            Object filterValue = meta.getFilterValue();
            if (filterValue != null && !filterValue.toString().isEmpty()) {
                if (filterValue.toString().contains("[")) {
                    String predicateTemp = "";
                    String[] array = filterValue.toString().substring(1, filterValue.toString().length() - 1).split(",");
                    if (array != null) {
                        try {
                            for (int i = 0; i < array.length; i++) {
                                //System.out.println(filterField + "=" + array[i].trim() + "&");
                                if (i == 0) {
                                    predicateTemp = filterField + "=" + array[i].trim() + "&" + predicateTemp;
                                } else if (i == 1) {
                                    predicateTemp = filterField + i + "=" + array[i].trim() + "&" + predicateTemp;
                                }
                            }
                            predicate += predicateTemp;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                } else {
                    predicate += filterField + "=" + filterValue + "&";
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
            Object[] forObject = (Object[]) restTemplate.getForObject(url + "?id=" + rowKey, this.entitiArray == null ? Object[].class
                    : this.entitiArray);
            if (forObject != null) {
                ob = (T) forObject[0];
            }
        } catch (RestClientException e) {
            Logger.getLogger(LazyModelWS.class
                    .getName()).log(Level.SEVERE, null, e);
        }
        return ob;
    }

    private Integer getCurrentPage(int first) {
        return (first / this.getPageSize());
        //return first;
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

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    @Override
    public String getRowKey(Object enttiClass) {
        if (enttiClass instanceof User) {
            return ((User) enttiClass).getId().toString();
        }
        return "";
    }

    @Override
    public int count(Map<String, FilterMeta> filterBy) {
        System.out.println("count");
//        if (result == null) {
//            return 0;
//        }
//        return (int) result.stream()
//                .filter(o -> Utils.filter(FacesContext.getCurrentInstance(), filterBy.values(), o))
//                .count();
//        String predicate = "";
//        predicate += formatFilter(filterBy);
//        predicate += formatFilter(getFilterss());
//        if (!url.contains("?")) {
//            urlFilter = url + "?" + predicate;
//        } else {
//            //char lastUrl = url.charAt(url.length() - 1);
//            urlFilter = url + "&" + predicate;
//        }
//
//        String tempUrl = String.format("%s", urlFilter);
//        System.out.println("//Url tempUrl " + tempUrl);
//        //ResponseEntity<Object[]> responseEntity = restTemplate.getForEntity(tempUrl, this.entitiArray == null ? Object[].class : this.entitiArray);
//        ResponseEntity<Object[]> responseEntity = restTemplate.exchange(tempUrl, HttpMethod.GET, entity, this.entitiArray == null ? Object[].class : this.entitiArray);
//        Object[] forObject = responseEntity.getBody();
//        if (forObject != null) {
//            HttpHeaders headers = responseEntity.getHeaders();
//            List<String> listSize = headers.get("rootSize");
//            System.out.println("headers: " + headers.toString());
//            if (listSize != null) {
//                this.setRowCount(Integer.valueOf(listSize.get(0)));
//                return Integer.valueOf(listSize.get(0));
//            }
//        }
//        return 0;
//         
        return getRowCount();

    }

}
