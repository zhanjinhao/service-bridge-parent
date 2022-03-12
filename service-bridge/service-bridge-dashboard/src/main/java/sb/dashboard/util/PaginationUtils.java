package sb.dashboard.util;

import java.util.Collections;
import java.util.List;

public class PaginationUtils {

    public static List Pagination(List<? extends Comparable> list, int page, int row) {

        int size = list.size();

        int beginIndex = ((page - 1) * row) > size ? 0 : (page - 1) * row;

        int endIndex = ((page) * row) > size ? size : (page) * row;

        Collections.sort(list);

        List paginationList = list.subList(beginIndex, endIndex);

        return paginationList;
    }

}
