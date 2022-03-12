package sb.dashboard.ui.service;


import sb.common.pojo.ui.PageResult;

public interface PsServiceInterface {

    PageResult getPsInfos(String nameLike, int page, int row);

    PageResult getPsJarName(String spsid);

    PageResult getAllScsid(String spsid, String serviceName, String version);
}
