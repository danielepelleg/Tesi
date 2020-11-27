import java.util.List;

@Mapper

public interface ConfigServicesRemoteExMapper 
                        extends ConfigServicesRemoteMapper {
    List<ConfigServicesRemote> 
                selectExByExample(ConfigServicesRemoteExample example);
}