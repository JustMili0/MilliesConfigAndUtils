*Note: keep TODO and README on the latest main branch you're working on

# TODO - 1.0
- Backport the whole config screen builder to 1.20.1
  - Fix Cats-In-Cats
  - Fix Builder Tabs
  - Give "Reset" a proper purpose
  - Add hover-over text for Reset and Undo buttons
    - Undo - Undoes last performed change
    - Reset - Resets selected key to default value
- (Cross-Loader API) Add cross-loader events
  - Straight up replicate the entirety of Fabric's event system adding whatever it's missing that Forge itself has for some reason
- (Config Lib) Fix builder tabs in config screen
- (Config Lib) Add ability to add custom images to entry descriptions (similar to YACL)
- (Config Lib) Add color picker entry
- (Config Lib) Add custom textures (on a 256x256 texture atlas(?)) for widgets
- (Config Lib) Add YAML/YML support
- (Cross-Loader API) Add cross-loader registries, networking and other
- Write full documentation

### YAML/YML - Design
```yaml
# a comment 1
# Allowed range: 0-10 - Default: 5
someInt: 5

# a comment 2
# a comment continuation (because of a second .comment() or \n )
# Allowed values: true, false - Default: true
someBool: true
  
# A category comment
someCategory:
  aCategoryWithNoCommentHenceNoSpacingFromParentCategory:
    # Default: Wawawa
    someOtherString: "Wawawa"

  # A category comment 
  aCategoryInCategory:
    # a comment 3
    # Default: Hello world
    someString: "Hello world"
    
  # a comment 4
  # Allowed range: 0-16 - Default: 8.0
  someDouble: 8.0
    
  # a lonely comment (from just a builder.comment() )
```