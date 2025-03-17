package com.smohtadi.expenses.dtos;

import com.smohtadi.expenses.exceptions.MalformedSortRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.Sort.Direction;

import java.util.ArrayList;
import java.util.List;

public class PagedRequest {
    private final int page;
    private final int pageSize;
    private final Sort sort;

    public static PagedRequest of(int page, int pageSize, String sort) {
        return new PagedRequest(page, pageSize, sort);
    }

    private PagedRequest(int page, int pageSize, String sort) {
        this.page = page;
        this.pageSize = pageSize;
        this.sort = parseSort(sort);
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Sort getSort() {
        return sort;
    }

    private Sort parseSort(String s) throws MalformedSortRequest {
        if (s == null || s.trim().isEmpty())
            return null;
        String[] words = s.split(",");
        List<Order> orders = new ArrayList<>();
        for (String tokens : words) {
            String[] token = tokens.split("-");
            if (token.length != 2 || token[0].trim().isEmpty() || !token[1].equalsIgnoreCase(Direction.ASC.name()) && !token[1].equalsIgnoreCase(Direction.DESC.name()))
                throw new MalformedSortRequest();
            Direction direction =
                    token[1].equalsIgnoreCase(Direction.ASC.name()) ?
                            Direction.ASC : Direction.DESC;
            Order order = new Order(direction, token[0]);
            orders.add(order);
        }
        if (!orders.isEmpty())
            return Sort.by(orders);
        return null;
    }
}
