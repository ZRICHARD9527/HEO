package cn.looyeagee.heo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class MyPage<T> {
    Integer totalPages;//总页数
    Integer currentRecordsCount;//当前页符合条件记录数
    Integer totalRecordsCount;//符合条件总记录数
    List<T> data;
}
