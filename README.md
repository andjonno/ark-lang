# Ark Lang
Functional language with Lisp style syntax and some syntactic sugar for great readability.

[![Build Status](https://travis-ci.org/andjonno/ark-lang.svg?branch=master)](https://travis-ci.org/andjonno/ark-lang) 
[![Maintainability](https://api.codeclimate.com/v1/badges/672b07fb2b7a95a3474e/maintainability)](https://codeclimate.com/github/andjonno/ark-lang/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/672b07fb2b7a95a3474e/test_coverage)](https://codeclimate.com/github/andjonno/ark-lang/test_coverage)

#### Ark
Ark is a hobbyist language not to be taken seriously or used in production!

Ark will eventually be strongly typed and will be a good hybrid between classical imperative OOP and Functional language
styles - leaning more towards the functional paradigm - aiming to get the best of both worlds.

#### My Motivations
###### Yet another hobbyist lang.

Why am I creating this language? Mostly to learn. I love de-mistifying things that we commonly take for granted. 
Language implementation is no exception and there is so much to gain by having a better understanding of what goes into 
building a programming language and how they work.

### Language Spec

[Checkout the Grammar](https://github.com/andjonno/ark-lang/blob/master/resources/grammar.txt)

##### Comments
```
;; this is a comment
```

##### Variable Declaration
```
let a = 1
print a ;; 1

let a = 2,
    b = (** a 2)
print b ;; 4
```

##### Strings

```
let a = "G'day world"
(out a) ;; G'day world
```

Strings are enumerable and can be used in a `for-in` enumerator expr.

##### Array

```
let a = [1,2,3,4]
(out (len a)) ;; 4
```

Arrays are enumerable and can be used in a `for-in` enumerator expr.

##### Numeric Operations
```
;; addition
(+ 1 2) ;; 3
;; subtraction
(- 1 2) ;; -1
```

```
;; multiplication
(* 2 3) ;; 6

;; division
(/ 6 3) ;; 2
```

```
;; modulo
(% 5 2) ;; 1
```

```
(< 1 2)  ;; true
(< 1 0)  ;; false
(> 1 0)  ;; true
(> 1 2)  ;; false
(>= 1 0) ;; true
(>= 1 1) ;; true
(>= 1 5) ;; false
(!= 1 2) ;; true
(!= 1 1) ;; false
```

##### Bitwise Operations

```
(^ 1 1)     ;; 0
(^ 1 2)     ;; 3
(& 3 1)     ;; 1
(<< 1 2)    ;; 4
(>> 2 1)    ;; 1
(>>> 3 1)   ;; 1
(| 3 1)     ;; 3
~0          ;; -1 (bitwise compliment)
```

##### Logic Operations
```
(and true false) ;; false
(and true true)  ;; true
(or true false)  ;; true
(or (> 1 2) (!= 1 3)) ;; true 
```

##### Lambdas

```
(lambda sum : a b -> (+ a b))
(sum 4 6) ;; 10
```

```
(lambda filter : c f -> {
  let a = []
  (i:c) {
    if (f i) {
      (add a i)
    }
  }
  send a
})
;; filter nums greater than 3
let nums = (filter [1,2,3,4,5] lambda : x -> (<= x 3))
(print nums) ;; [1,2,3]
```

##### Range Expressions

Shorthand syntax to generate a range from a lower to an upper bound.
```
1..5 ;; [1,2,3,4]
1...5 ;; [1,2,3,4,5]
```

A range expression resolves to an Array.

##### For-In Enumerable Iteration

```
for x in 1...5 {
  (out x)
}
;; 1
;; 2
;; 3
;; 4
;; 5
```

With index identifier:

```
for x, i in 1...5 {
  (out x i)
}
;; 1 0
;; 2 1
;; 3 2
;; 4 3
;; 5 4
```


##### Sending (Coming soon)

This is probably my favourite feature of Ark. Using `sends` adds so much clarity. You will see below how data flows 
from the left to the right. Rather than nesting function calls, expression are evaluated from the left to the right...

```
[1,2,3,4] -> (pow 2) ;; [1,4,9,16]
    -> (filter lambda | x -> (< x 5)) ;; [1,4]
    -> (lambda | x -> Point(x:x, 0)) ;; [Point(1,0), Point(4,0)]
```

##### More features to come! 💃

#### Language Roadmap

- Strong typing
- Structs
- Standard Library
    - File I/O
    - Network I/O (TCP/IP, HTTP)
    - I/O Streams
    - Regex
    - Math
    - Data Structures (Stacks, Queues, Heaps, etc)
- Dependency management (inspired by Golang github urls)
- Unit Testing Framework
- Static Analyser for IDE Integration
- Syntax Themes for Sublime, Atom, etc


#### Dev Install

1. Open Ark prompt `$ ./ark`
2. Or, execute an ark file `$ ./ark [file]`
3. ⌨ Have fun!
