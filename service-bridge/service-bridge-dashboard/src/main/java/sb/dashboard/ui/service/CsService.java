package sb.dashboard.ui.service;

import org.springframework.stereotype.Service;
import sb.common.pojo.ui.CsInfo;
import sb.common.pojo.ui.PageResult;
import sb.core.buffer.ConsumerSystem;
import sb.core.buffer.ConsumerSystemFactory;
import sb.core.buffer.ServiceKey;
import sb.core.buffer.Skeleton;
import sb.dashboard.util.PaginationUtils;

import java.util.*;

@Service
public class CsService implements CsServiceInterface {
    @Override
    public PageResult getCsInfos(String nameLike, int page, int row) {

        Map<String, ConsumerSystem> consumerSystemsMap = ConsumerSystemFactory.getConsumerSystemMap();

        Set<Map.Entry<String, ConsumerSystem>> entries = consumerSystemsMap.entrySet();

        Iterator<Map.Entry<String, ConsumerSystem>> iterator = entries.iterator();

        List<CsInfo> csInfos = new ArrayList<>();

        // 每个 CS
        while (iterator.hasNext()) {
            Map.Entry<String, ConsumerSystem> next = iterator.next();
            String scsid = next.getKey();
            ConsumerSystem consumerSystem = next.getValue();

            Map<String, List<Skeleton>> skeletons = consumerSystem.getSkeletonBuffer();
            Set<Map.Entry<String, List<Skeleton>>> entries1 = skeletons.entrySet();
            Iterator<Map.Entry<String, List<Skeleton>>> iterator1 = entries1.iterator();

            // 每个 PS
            while (iterator1.hasNext()) {
                Map.Entry<String, List<Skeleton>> next1 = iterator1.next();
                String spsid = next1.getKey();
                List<Skeleton> value = next1.getValue();
                Iterator<Skeleton> iterator2 = value.iterator();

                // 每个 Skeleton
                while(iterator2.hasNext()){
                    Skeleton next2 = iterator2.next();
                    ServiceKey serviceKey = next2.getServiceKey();
                    csInfos.add(new CsInfo(scsid, spsid, serviceKey.getServiceName(), serviceKey.getVersion(), CsInfo.BUILD));
                }
            }
        }

        if (nameLike != null && !("".equals(nameLike))) {
            Iterator<CsInfo> iterator1 = csInfos.iterator();
            while (iterator1.hasNext()) {
                CsInfo next = iterator1.next();
                if (!next.getServiceName().contains(nameLike)) {
                    iterator1.remove();
                }
            }
        }

        List pagination = PaginationUtils.Pagination(csInfos, page, row);

        return new PageResult(csInfos.size(), pagination);
    }
}
