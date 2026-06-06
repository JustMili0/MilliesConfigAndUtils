*Note: keep TODO and README on the latest main branch you're working on

# TODO - 1.0
- Backport the whole config screen builder to 1.20.1
  - Fix Inputs' Red Text becoming white again when exceeding 10 characters
  - Fix Undo not undoing new list key entries nor redoing them
  - Fix list entry inputs that are inline with the list name are clickable, but not editable
  - Make each entry have its own selectable "section" so "Reset" know what to reset
  - Give "Reset" button a proper function
  - Let number entries and enum entries be sliders

- (Cross-Loader API) Add cross-loader events
  - Replicate all of Fabric's and Forge's event triggers in Fabric's event format
  - Note: PortingLib has a lot of Forge's events already ported (but not all), and we were given the permission to just copy it

- (Config Lib) Add enum selector entry
- (Config Lib) Add color picker entry
- (Config Lib) Add ability to add custom images to entry descriptions (similar to YACL)
- (Config Lib) Add YAML/YML support

- (Cross-Loader API) Add cross-loader registries, networking and other

- (Config Lib) Add TOML support

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