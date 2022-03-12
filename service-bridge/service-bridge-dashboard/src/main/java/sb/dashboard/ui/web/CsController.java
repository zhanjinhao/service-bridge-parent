package sb.dashboard.ui.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sb.core.ui.pojo.PageResult;
import sb.core.ui.service.CsServiceInterface;

@RestController
@RequestMapping("cs")
public class CsController {

    private final static Logger LOGGER = LoggerFactory.getLogger(CsController.class);

    private CsServiceInterface csServiceInterface;

    @Autowired
    public CsController(CsServiceInterface csServiceInterface) {
        this.csServiceInterface = csServiceInterface;
    }

    @RequestMapping("getCsInfos")
    public PageResult getCsInfos(
            @RequestParam("nameLike") String nameLike,
            @RequestParam("page") int page,
            @RequestParam("row") int row) {
        try {
            return csServiceInterface.getCsInfos(nameLike, page, row);
        } catch (Exception e) {
            LOGGER.error("getPsInfos error", e);
            return PageResult.getPageResultNull();
        }
    }

}
