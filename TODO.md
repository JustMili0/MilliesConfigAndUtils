*Note: keep TODO and README on the latest main branch you're working on

# TODO - 1.1
- Fix builder tabs in config screen
- Add ability to add custom images to entry descriptions (similar to YACL)
- Add color picker entry
- Add custom textures (on a 256x256 texture atlas(?)) for widgets
- Add YAML/YML support

# TODO - 1.2
- Add datagen utils and everything else that you commonly use
- Add multiloader API stuff (registry, events, networking, etc., basically Architectury API)
- Rebrand mod to `Millie's Libs & API`
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