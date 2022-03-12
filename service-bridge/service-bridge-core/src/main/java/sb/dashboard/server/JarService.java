package sb.dashboard.server;

import sb.common.pojo.ui.JarInfo;
import sb.common.pojo.ui.PageResult;
import sb.core.init.config.ApplicationConfig;
import sb.core.buffer.ProviderSystem;
import sb.core.buffer.ProviderSystemFactory;
import sb.core.buffer.URLClassLoaderWrap;
import sb.dashboard.util.PaginationUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

public class JarService {

    public PageResult getJarInfos(int page, int row) {
        List<JarInfo> allJarInfos = getAllJarInfos();

        List pagination = PaginationUtils.Pagination(allJarInfos, page, row);

        return new PageResult(allJarInfos.size(), pagination);
    }

    public PageResult searchJarInfos(String nameLike, int page, int row) {

        List<JarInfo> allJarInfos = getAllJarInfos();

        Iterator<JarInfo> iterator = allJarInfos.iterator();

        while (iterator.hasNext()) {
            JarInfo jarInfo = iterator.next();

            if (!jarInfo.getName().contains(nameLike)) {
                iterator.remove();
            }
        }

        List pagination = PaginationUtils.Pagination(allJarInfos, page, row);

        return new PageResult(allJarInfos.size(), pagination);
    }


    public PageResult getAllSpsid() {

        Map<String, ProviderSystem> providerSystemsMap = ProviderSystemFactory.getProviderSystemMap();
        Set<Map.Entry<String, ProviderSystem>> entries = providerSystemsMap.entrySet();
        Iterator<Map.Entry<String, ProviderSystem>> iterator = entries.iterator();

        List<String> spsids = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String, ProviderSystem> providerSystem = iterator.next();
            spsids.add(providerSystem.getKey());
        }

        return new PageResult(0, spsids);

    }

    private static List<String> getJarInfosInJarDirectory() {
        String jarPath = ApplicationConfig.JAR_PATH;
        File file = new File(jarPath);
        File[] jars = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getPath().endsWith("jar"))
                    return true;
                return false;
            }
        });
        List<String> jarNames = new ArrayList<>();
        for (File jar : jars) {
            jarNames.add(jar.getName());
        }
        return jarNames;
    }

    private static List<JarInfo> getJarInfosInConsumerSystems() {
        Map<String, ProviderSystem> providerSystemsMap = ProviderSystemFactory.getProviderSystemMap();
        Set<Map.Entry<String, ProviderSystem>> entries = providerSystemsMap.entrySet();
        Iterator<Map.Entry<String, ProviderSystem>> iterator = entries.iterator();
        List<JarInfo> jarInfos = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String, ProviderSystem> providerSystem = iterator.next();
            Map<String, URLClassLoaderWrap> urlClassLoaderWrapMap = providerSystem.getValue().getUrlClassLoaderWrapBuffer();
            Set<Map.Entry<String, URLClassLoaderWrap>> entries1 = urlClassLoaderWrapMap.entrySet();
            Iterator<Map.Entry<String, URLClassLoaderWrap>> iterator1 = entries1.iterator();

            while (iterator1.hasNext()) {
                Map.Entry<String, URLClassLoaderWrap> next = iterator1.next();
                URLClassLoaderWrap urlClassLoaderWrap = next.getValue();
                String jarName = urlClassLoaderWrap.getJarName();
                String spsid = urlClassLoaderWrap.getSpsid();
                JarInfo jarInfo = new JarInfo(jarName, spsid, JarInfo.LOAD);
                jarInfos.add(jarInfo);
            }
        }
        return jarInfos;
    }

    private List<JarInfo> getAllJarInfos() {
        List<String> jarInfosInJarDirectory = getJarInfosInJarDirectory();
        List<JarInfo> jarInfosInConsumerSystems = getJarInfosInConsumerSystems();

        Iterator<String> iterator = jarInfosInJarDirectory.iterator();
        while (iterator.hasNext()) {
            String jarName = iterator.next();

            boolean isLoad = false;
            Iterator<JarInfo> iterator1 = jarInfosInConsumerSystems.iterator();
            while (iterator1.hasNext()) {
                JarInfo next = iterator1.next();
                if (jarName.equals(next.getName())) {
                    isLoad = true;
                    break;
                }
            }
            if (!isLoad) {
                JarInfo jarInfo = new JarInfo(jarName, "/", JarInfo.UN_LOAD);
                jarInfosInConsumerSystems.add(jarInfo);
            }
        }

        return jarInfosInConsumerSystems;
    }

}