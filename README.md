# Dove

Dove is a spigot Utility library reliant on [Iron Pipe](https://github.com/Y2Kwastaken/IronPipe)

## Adding Dove to your project

### Maven

```xml

<repository>
    <id>miles-repos-libraries</id>
    <name>Miles Repositories</name>
    <url>https://maven.miles.sh/libraries</url>
</repository>

<dependency>
    <groupId>sh.miles.dove</groupId>
    <artifactId>Dove</artifactId>
    <version>2023.10.30</version>
</dependency>
```

### Gradle

```
implementation("sh.miles.dove:Dove:2023.10.30")
```

## Setting Up Dove

Setting up dove is very easy to do. You can start using dove in your project by doing the following

```java
public class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Dove.init(this, new ArrayList());
    }
}
```

## Using Dove

Dove has many useful utilities some of them will be covered here

### JsonHelper

The json helper is a useful helper class for reading and writing to and from json files.
JsonHelper utilizes Gson to power it. You can create an adapter by implementing JsonAdapter class
an example is below.

```java
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import sh.miles.dove.configuration.JsonAdapter;

import java.lang.reflect.Type;
import java.util.UUID;

public class UUIDAdapter implements JsonAdapter<UUID> {

    @Override
    public UUID deserialize(final JsonElement element, final Type type, final JsonDeserializationContext context) throws JsonParseException {
        return UUID.fromString(element.getAsString());
    }

    @Override
    public JsonElement serialize(final UUID uuid, final Type type, final JsonSerializationContext context) {
        return new JsonPrimitive(uuid.toString());
    }
}
```

Keep in mind that this example is simple. JsonAdapter is best used on more complex object types.

you can register your type adapter in the init function

```java
public class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Dove.init(this, List.of(new UUIDAdapter()));
    }
}
```

### JsonConfig

JsonConfig is an alternative to the Bukkit configuration and can be used in a similar way.

```java
import sh.miles.dove.Dove;
import sh.miles.dove.configuration.JsonConfig;

public class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Dove.init(this, new ArrayList());
        JsonConfig config = new JsonConfig(Dove.getJsonHelper().getGson(), this, "test-config.json");
        boolean isDoveBeingUsed = config.getBoolean("am-i-using-dove");
        Dove.log().info("Dove's Status is: " + isDoveBeingUsed);
    }
}
```

### Dependency Registry

Dove provides an excellent method for managing your registries. There is a provided DependencyRegistry class
which allows ease in managing dependencies.

Below is a simple example of the possible uses of the DependencyRegistry with Vault!

```java
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultDependency implements EconomyPluginDependency<Plugin> {

    public static final String KEY = "vault";

    private Plugin plugin;
    private Economy economy;
    private boolean loaded;

    @Override
    public void load(@NotNull final Plugin plugin) {
        this.plugin = Bukkit.getPluginManager().getPlugin("Vault");
        if (this.plugin == null) {
            return;
        }
        this.plugin = plugin;
        final RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (economy == null) {
            throw new RuntimeException("Unable to find economy provider, could not load Vault Hook");
        }
        economy = rsp.getProvider();
        loaded = true;
    }

    @Override
    public void unload(@NotNull final Plugin plugin) {
        economy = null;
        loaded = false;
    }

    @Override
    public double getBalance(final UUID uuid) {
        return economy.getBalance(wrap(uuid));
    }

    @Override
    public boolean withdraw(final UUID uuid, final double v) throws UnsupportedOperationException {
        EconomyResponse response = economy.withdrawPlayer(wrap(uuid), v);
        EconomyResponse.ResponseType type = response.type;
        switch (type) {
            case SUCCESS, FAILURE -> {
                return true;
            }
            default -> throw new RuntimeException("Unable to withdraw on unimplemented system");
        }
    }

    @Override
    public boolean deposit(final UUID uuid, final double v) {
        EconomyResponse response = economy.depositPlayer(wrap(uuid), v);
        EconomyResponse.ResponseType type = response.type;
        switch (type) {
            case SUCCESS -> {
                return true;
            }
            case NOT_IMPLEMENTED -> throw new RuntimeException("Unable to deposit on unimplemented system");
            default -> {
                return false;
            }
        }
    }

    @Override
    public boolean isLoaded() {
        return this.loaded;
    }

    @Nullable
    @Override
    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    private OfflinePlayer wrap(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid);
    }
}
```

Registering the Dependency is equally simple

```java
import org.bukkit.Bukkit;
import sh.miles.dove.Dove;

public class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Dove.init(this, new ArrayList());
        Dove.getDependencyRegistry().register(new VaultDependency());
        VaultDependency dependency = Dove.getDependencyRegistry().getEnabled(VaultDependency.class);
        for (final Player player : Bukkit.getOnlinePlayers()) {
            dependency.deposit(player.getUniqueId(), 1);
        }
    }
}
```

Dove has many other utilities that can make development much faster, this is just the tip of the iceberg!
