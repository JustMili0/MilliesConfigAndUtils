# TODO - 1.0
- Add JSON and JSON5 support

- Add categories/hierarchy

- Make it so when a new config key is added to or moved in a mod config and there already is an existing config, it'll just add it or move it where it should be.
  - Can not change values of already existing keys that are different from default (aka changed by user)

- Add support for lists
  - bringing total support to ints, doubles, longs, floats, strings, booleans and lists

- Write documentation

# TODO - 1.1
- Add YAML/YML support

- Add some form of support for GUI config readers or make my own

# Plans how to make it look
*Comments add additional spacing
### Properties - no categories/hierarchy.
Ignore anything about creating categories and print in log when creating the file that categories are unsupported
```properties
# a comment 1
# Allowed range: 0-10 - Default: 5
someInt=5

# a comment 2
# a comment continuation (because of a second .comment() or \n )
# Allowed values: true, false - Default: true
someBool=true

# a comment 3
# Default: Hello world
someString="Hello world"

# a comment 4
# Allowed range: 0-16 - Default: 8.0
someDouble=8.0

# a lonely comment (from just a builder.comment() )
```

### JSON - no comments
Ignore anything about creating comments and print in log when creating the file that comments are unsupported
```json
{
  "h0": "Allowed range: 0-10 - Default: 5",
  "someInt": 5,
  "h1": "Allowed range: 0-12 - Default: 6.0",
  "someDouble": 6.0,
  
  "someCategory": {
    "aCategoryWithNoCommentHenceNoSpacingFromParentCategory": {
      "h0": "Default: Wawawa",
      "someOtherString": "Wawawa"
    },
    
    "aCategoryInCategory": {
      "h0": "Allowed range: 0-14 - Default: 7.0",
      "someString": "Hello world"
    },
    
    "h0": "Allowed range: 0-16 - Default: 8.0",
    "someDouble": 8.0
  }
}
```

### JSON5
```json5
{
  // a comment 1
  "h0": "Allowed range: 0-10 - Default: 5",
  "someInt": 5,
  
  // a comment 2
  // a comment continuation (because of a second .comment() or \n )
  "h1": "Allowed range: 0-12 - Default: 6.0",
  "someDouble": 6.0,
  
  // A category comment
  "someCategory": {
    "aCategoryWithNoCommentHenceNoSpacingFromParentCategory": {
      "h0": "Default: Wawawa",
      "someOtherString": "Wawawa"
    },
    
    // A category comment
    "aCategoryInCategory": {
      // a comment 3
      "h0": "Default: Hello world",
      "someString": "Hello world"
    },
    
    // a comment 4
    "h0": "Allowed range: 0-16 - Default: 8.0",
    "someDouble": 8.0,
    
    // a lonely comment (from just a builder.comment() )
  }
}
```

### YAML/YML
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