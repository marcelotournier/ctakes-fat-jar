# CTakes Pipeline Demo

A Demo for the CTakes clinical NLP pipeline, with all resources packaged in a fat jar.

## Instructions
1. Install java 8
2. Install Apache Maven
3. Build the jar with `mvn clean package`

## Usage:
There is a packaged Ammonite Scala console for the tests.

Build the fat jar with  `mvn clean package` and 
run it with `java -jar target/ctakes-1.0-SNAPSHOT.jar`:

```scala
import life.inova.nlp.ctakes.TaggerPipeline

// Get a pipeline to work with:
val pipe = new TaggerPipeline()
pipe.init()

val text = "Morquio syndrome (referred to as mucopolysaccharidosis IV, MPS IV, Morquio-Brailsford syndrome, or Morquio) is a rare metabolic disorder in which the body cannot process certain types of mucopolysaccharides (long chains of sugar molecules), which the body uses as lubricants and shock absorbers"


// Process the text in multiple possible output formats
val results = pipe.processToJSON(text)
val resultsXML = pipe.processToXML(text)

// write outputs
reflect.io.File("results.json").writeAll(results)
reflect.io.File("results.xml").writeAll(resultsXML.replace("><", ">\n<"))
```

## TODOs
- Fix exceptions related to file paths (works only in `./target` for now)
- Make it compatible with new UMLS versions
- Write python bindings

## References
- https://ctakes.apache.org
- https://github.com/healthnlp/examples
- https://github.com/lihaoyi/Ammonite
