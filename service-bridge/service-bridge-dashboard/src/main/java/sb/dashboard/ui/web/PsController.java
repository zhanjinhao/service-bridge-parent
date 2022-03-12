package sb.dashboard.ui.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sb.core.ui.pojo.PageResult;
import sb.core.ui.service.PsServiceInterface;
import sb.dashboard.ui.service.PsServiceInterface;

@RestController
@RequestMapping("ps")
public class PsController {

    private final static Logger LOGGER = LoggerFactory.getLogger(PsController.class);

    private PsServiceInterface psServiceInterface;

    @Autowired
    public PsController(PsServiceInterface psServiceInterface) {
        this.psServiceInterface = psServiceInterface;
    }

    @RequestMapping("getPsInfos")
    public PageResult getPsInfos(
            @RequestParam("nameLike") String nameLike,
            @RequestParam("page") int page,
            @RequestParam("row") int row) {
        try {
            return psServiceInterface.getPsInfos(nameLike, page, row);
        } catch (Exception e) {
            LOGGER.error("getPsInfos error", e);
            return PageResult.getPageResultNull();
        }
    }

    @RequestMapping("getPsJarName")
    public PageResult getPsJarName(@RequestParam("spsid") String spsid) {
        try {
            return psServiceInterface.getPsJarName(spsid);
        } catch (Exception e) {
            LOGGER.error("getPsInfos error", e);
            return PageResult.getPageResultNull();
        }
    }

    @RequestMapping("getAllScsid")
    public PageResult getAllScsid(
            @RequestParam("spsid") String spsid,
            @RequestParam("serviceName") String serviceName,
            @RequestParam("version") String version) {
        try {
            return psServiceInterface.getAllScsid(spsid, serviceName, version);
        } catch (Exception e) {
            LOGGER.error("getPsInfos error", e);
            return PageResult.getPageResultNull();
        }
    }

}
