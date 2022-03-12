package sb.dashboard.ui.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sb.core.ui.pojo.PageResult;
import sb.core.ui.service.JarServiceInterface;

@RestController
@RequestMapping("jar")
public class JarController {

    private final static Logger LOGGER = LoggerFactory.getLogger(JarController.class);

    private JarServiceInterface jarServiceInterface;

    @Autowired
    public JarController(JarServiceInterface jarServiceInterface) {
        this.jarServiceInterface = jarServiceInterface;
    }

    @RequestMapping("getJarInfos")
    public PageResult getJarInfos(@RequestParam("nameLike")String nameLike, @RequestParam("page") int page, @RequestParam("row") int row) {
        try {
            if(nameLike == null || "".equals(nameLike))
                return jarServiceInterface.getJarInfos(page, row);
            return jarServiceInterface.searchJarInfos(nameLike, page, row);
        } catch (Exception e) {
            LOGGER.error("getJarInfos error", e);
            return PageResult.getPageResultNull();
        }
    }

    @RequestMapping("getAllSpsid")
    public PageResult getAllSpsid() {
        try {
            return jarServiceInterface.getAllSpsid();
        } catch (Exception e) {
            LOGGER.error("getAllSpsid error", e);
            return PageResult.getPageResultNull();
        }
    }

}
