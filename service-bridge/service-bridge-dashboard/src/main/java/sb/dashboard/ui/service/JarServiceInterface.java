package sb.dashboard.ui.service;


import sb.common.pojo.ui.PageResult;

public interface JarServiceInterface {

    PageResult getJarInfos(int page, int row);

    PageResult searchJarInfos(String nameLike, int page, int row);

    PageResult getAllSpsid();
}
