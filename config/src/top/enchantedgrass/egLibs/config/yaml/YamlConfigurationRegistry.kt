package top.enchantedgrass.egLibs.config.yaml

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import net.kyori.adventure.key.Key
import org.slf4j.Logger
import top.enchantedgrass.egLibs.config.ConfigurationRegistry
import java.io.InputStream
import java.nio.file.Path
import kotlin.reflect.KClass

/**
 * The container to load and store [YamlConfiguration]s.
 */
object YamlConfigurationRegistry : ConfigurationRegistry<YamlConfiguration>() {
    internal val mapper = YAMLMapper()

    internal var dataDirectory: Path = Path.of("")
    internal var getResource: (String) -> InputStream? = { null }

    private lateinit var keyFactory: (String) -> Key

    /**
     * Initializes this registry.
     *
     * @param keyFactory The factory to create [Key]s.
     * @param dataDirectory The parent directory to store the configurations. Defaults to the current directory.
     * @param getResource The function to get the default resource. Defaults to null.
     * @param logger The logger to log the loading and unloading of configurations. Defaults to null.
     * @throws IllegalStateException If this registry is already initialized.
     */
    fun init(
        keyFactory: (String) -> Key,
        dataDirectory: Path = Path.of(""),
        getResource: (String) -> InputStream? = { null },
        logger: Logger? = null,
    ) {
        check(!this::keyFactory.isInitialized) { "YamlConfigurationRegistry is already initialized." }
        this.keyFactory = keyFactory
        this.dataDirectory = dataDirectory
        this.getResource = getResource
        this.logger = logger
    }

    override var logger: Logger? = null

    override fun <T : Any> create(name: String, type: KClass<T>) = YamlConfiguration(name, type, keyFactory(name))
}
