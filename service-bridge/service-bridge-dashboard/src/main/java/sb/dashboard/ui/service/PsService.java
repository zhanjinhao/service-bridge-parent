package sb.dashboard.ui.service;

import org.springframework.stereotype.Service;
import sb.common.pojo.ui.PageResult;
import sb.common.pojo.ui.PsInfo;
import sb.core.util.PaginationUtils;
import sb.pull.ProvidedServiceKey;

import java.rmi.server.Skeleton;
import java.util.*;

@Service
public class PsService implements PsServiceInterface {
    @Override
    public PageResult getPsInfos(String nameLike, int page, int row) {

        Map<String, ProviderSystem> providerSystemsMap = ProviderSystemFactory.getProviderSystemMap();

        Set<Map.Entry<String, ProviderSystem>> entries = providerSystemsMap.entrySet();

        Iterator<Map.Entry<String, ProviderSystem>> iterator = entries.iterator();

        List<PsInfo> psInfos = new ArrayList<>();

        // 遍历所有的 ProviderSystem
        while (iterator.hasNext()) {
            Map.Entry<String, ProviderSystem> providerSystemEntry = iterator.next();
            ProviderSystem providerSystem = providerSystemEntry.getValue();
            String spsid = providerSystemEntry.getKey();

            Map<ServiceKey, Stub> servicesBuffer = providerSystem.getStubBuffer();

            // serviceBuffer 缓存的桩信息
            Iterator<Map.Entry<ServiceKey, Stub>> serviceBufferIterator = servicesBuffer.entrySet().iterator();
            while (serviceBufferIterator.hasNext()) {
                Map.Entry<ServiceKey, Stub> next = serviceBufferIterator.next();
                ServiceKey key = next.getKey();
                Stub stub = next.getValue();
                PsInfo psInfo = new PsInfo(spsid, stub.getUrlClassLoaderWrap().getJarName(), key.getServiceName(), key.getVersion(), PsInfo.BUILD);
                psInfos.add(psInfo);
            }

            // 从注册中心获取的数据
            List<ProvidedServiceKey> allServiceKey = providerSystem.getPullServiceDriver().getAllServiceKey(providerSystem.getProviderSystemConfig());

            if (allServiceKey == null)
                continue;
            Iterator<ProvidedServiceKey> providedServiceKeyIterator = allServiceKey.iterator();
            while (providedServiceKeyIterator.hasNext()) {
                ProvidedServiceKey next = providedServiceKeyIterator.next();
                ServiceKey serviceKey = new ServiceKey(next.getName(), next.getVersion());
                if (!servicesBuffer.containsKey(serviceKey)) {
                    PsInfo psInfo = new PsInfo(spsid, "/", serviceKey.getServiceName(), serviceKey.getVersion(), PsInfo.UN_BUILD);
                    psInfos.add(psInfo);
                }
            }
        }

        if (nameLike != null && !("".equals(nameLike))) {
            Iterator<PsInfo> iterator1 = psInfos.iterator();
            while (iterator1.hasNext()) {
                PsInfo next = iterator1.next();
                if (!next.getServiceName().contains(nameLike)) {
                    iterator1.remove();
                }
            }
        }

        List pagination = PaginationUtils.Pagination(psInfos, row, page);

        return new PageResult(psInfos.size(), pagination);
    }

    @Override
    public PageResult getPsJarName(String spsid) {

        ProviderSystem providerSystem = ProviderSystemFactory.getProviderSystem(spsid);

        Map<String, URLClassLoaderWrap> urlClassLoaderWrapMap = providerSystem.getUrlClassLoaderWrapBuffer();

        Set<String> strings = urlClassLoaderWrapMap.keySet();

        List<String> jarNames = new ArrayList<>();

        Iterator<String> iterator = strings.iterator();

        while (iterator.hasNext()) {
            String next = iterator.next();
            jarNames.add(next);

        }

        return new PageResult(strings.size(), jarNames);

    }

    @Override
    public PageResult getAllScsid(String spsid, String serviceName, String version) {

        ServiceKey serviceKey = new ServiceKey(serviceName, version);

        List<String> scsids = new ArrayList<>();

        Map<String, ConsumerSystem> consumerSystemsMap = ConsumerSystemFactory.getConsumerSystemMap();
        Iterator<Map.Entry<String, ConsumerSystem>> csIterator = consumerSystemsMap.entrySet().iterator();

        // 每个CS
        while (csIterator.hasNext()) {
            Map.Entry<String, ConsumerSystem> next = csIterator.next();

            ConsumerSystem consumerSystem = next.getValue();
            String scsid = next.getKey();
            Map<String, List<Skeleton>> skeletonsMap = consumerSystem.getSkeletonBuffer();

            // 获得PS对应的ID
            List<Skeleton> skeletons = skeletonsMap.get(spsid);

            if (skeletons == null || skeletons.size() == 0) {
                scsids.add(scsid);
                continue;
            }

            Iterator<Skeleton> skeletonIterator = skeletons.iterator();

            boolean flag = false;
            while (skeletonIterator.hasNext()) {
                Skeleton next2 = skeletonIterator.next();
                if (next2.getServiceKey().equals(serviceKey)) {
                    flag = true;
                    break;
                }
            }
            if(!flag) {
                scsids.add(scsid);
            }
        }

        return new PageResult(scsids.size(), scsids);
    }
}
