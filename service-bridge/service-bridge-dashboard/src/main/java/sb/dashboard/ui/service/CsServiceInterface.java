package sb.dashboard.ui.service;


import sb.common.pojo.ui.PageResult;

public interface CsServiceInterface {

    PageResult getCsInfos(String nameLike, int page, int row);

}
