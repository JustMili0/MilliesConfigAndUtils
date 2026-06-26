*Note: keep TODO and README on the latest main branch you're working on

# TODO - 1.0
- Backport the whole config screen builder to 1.20.1
  - Fix list entry inputs that are inline with the list name are clickable, but not editable
    - Undo the change of putting first input inline with text, just put it lower like with e.g. categories
  - Make each entry have its own selectable "section" so "Reset" know what to reset
  - Give "Reset" button a proper function
  - Let number entries and enum entries be sliders

- (Config Lib) Add enum selector entry
- (Config Lib) Add color picker entry
- (Config Lib) Add ability to add custom images to entry descriptions (similar to YACL)
- (Config Lib) Add YAML/YML support

- (Cross-Loader API) Add cross-loader events accordingly to `events/package-info.java`
  - Create mixins for event invokers (additionally provide links to Fabric API's github repository linked to mixin source)
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