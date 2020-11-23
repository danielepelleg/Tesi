package it.mapsgroup.zerocoda.booking.persistence.mapper;

import it.mapsgroup.zerocoda.booking.persistence.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper

public interface ConfigServicesRemoteExMapper 
                        extends ConfigServicesRemoteMapper {
    List<ConfigServicesRemote> 
                selectExByExample(ConfigServicesRemoteExample example);
}