package it.mapsgroup.zerocoda.booking.service;

import it.mapsgroup.commons.collect.Tuple2;
import it.mapsgroup.zerocoda.booking.dto.Result;
import it.mapsgroup.zerocoda.booking.dto.v1.facility.Facility;
import it.mapsgroup.zerocoda.booking.persistence.dao.ConfigMryouEnterpriseDao;
import it.mapsgroup.zerocoda.booking.persistence.dto.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class FacilityService {
    private static final Logger LOG = getLogger(FacilityService.class);
    private final ConfigMryouEnterpriseDao configMryouEnterpriseDao;
    private final DtoMapper dtoMapper;

    @Autowired
    public FacilityService(ConfigMryouEnterpriseDao configMryouEnterpriseDao,
                           DtoMapper dtoMapper) {
        this.configMryouEnterpriseDao = configMryouEnterpriseDao;
        this.dtoMapper = dtoMapper;
    }

    /**
     * Retrieve the facilities fot the given ApiKey ID
     *
     * @param apiId the host's api key ID
     * @param forwardedFor the host's IP address
     * @return
     */
    public Result<Facility> find(Integer apiId, 
                                String forwardedFor, String lang) {
        LOG.debug("Looking for facilities [apiId={}, forwardedFor={}]", 
                                apiId, forwardedFor);
        ConfigMryouEnterpriseFilter filter = new ConfigMryouEnterpriseFilter();

        // filter settings
        filter.setAndIsActive(true);
        filter.setAndApiKey(apiId);
        filter.setAndVisibleInWeeks(1);
        filter.setGroupByClause("CME.id, CSR.id");
        filter.setOrderByClause("CME.id, CSR.id");

        List<ConfigMryouEnterpriseEx> enterprises = configMryouEnterpriseDao.findActive(filter);
        List<Tuple2<ConfigMryouEnterpriseWithBLOBs, List<ConfigServicesRemote>>> orderedEnterprises =
                DaoHelper.orderFacilities(enterprises);
        List<Facility> ret = orderedEnterprises.stream()
                .map(t -> dtoMapper.copy(t.first(), t.second(), lang))
                .collect(Collectors.toList());
        return new Result<>(ret, ret.size());
    }
}
