## LetterQ

``` 
LetterQ(expr)
``` 
> tests whether `expr` is a string, which only contains letters.

A character is considered to be a letter if its general category type, provided by the Java method `Character#getType()` is any of the following:
* `UPPERCASE_LETTER`
* `LOWERCASE_LETTER`
* `TITLECASE_LETTER`
* `MODIFIER_LETTER`
* `OTHER_LETTER`
 
Not all letters have case. Many characters are letters but are neither uppercase nor lowercase nor titlecase.
 