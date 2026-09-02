*Note: keep TODO and README on the latest main branch you're working on

# TODO
- Start using the Stonecutter template for multi-version, split the mod into:
  - `net.justmili.corelibs` (Main), 
  - `net.justmili.utils` (utils and other unspecified stuff), 
  - `net.justmili.config` and `net.justmili.configscreen`, 
  - `net.justmili.api`
  - (Similarly to how Fabric API is split into multiple jars in jar)
- [AWAITING] (Cross-Loader API) Add cross-loader networking
- Finish up datagen utils, fully implementing all blocks with special models

- Finish base of the config library
  - Rewrite if possible to use codecs
  - ***Make COMMON_SERVER_PRIORITY only override in-memory rather than on-file***
  - ***Make syncing COMMON_SERVER_PRIORITY files use the same system between Fabric and Forge***
  - Fix large doubles with an exponent ex. "1.1E12" not being edited
    - Probably a parsing issue
  - Make each entry have its own selectable "section" so "Reset" know what to reset
  - Give "Reset" button a proper function
  - Let number entries and enum entries be sliders

- (Config Lib) Add enum selector entry
- (Config Lib) Add color picker entry
- (Config Lib) Add ability to add custom images to entry descriptions (similar to YACL)

- (Cross-Loader API) Add cross-loader events accordingly to `events/package-info.java`
- (Cross-Loader API) Add cross-loader registries

- (Config Lib) Add TOML support

- Write full documentation