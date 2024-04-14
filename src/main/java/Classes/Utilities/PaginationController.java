package Classes.Utilities;

import java.util.ArrayList;
import java.util.List;
public class PaginationController<T> {
    private List<T> dataList;
    private int pageSize;
    private int currentPage;

    public PaginationController(List<T> dataList, int pageSize) {
        this.dataList = dataList;
        this.pageSize = pageSize;
        this.currentPage = 0;
    }

    public List<T> getDataForPage(int page) {
        int startIndex = page * pageSize;
        int endIndex = Math.min(startIndex + pageSize, dataList.size());
        return dataList.subList(startIndex, endIndex);
    }

    public void nextPage() {
        currentPage++;
        currentPage = Math.min(currentPage, getTotalPages() - 1);
    }

    public void previousPage() {
        currentPage--;
        currentPage = Math.max(currentPage, 0);
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) dataList.size() / pageSize);
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
