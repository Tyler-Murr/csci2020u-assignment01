// TODO: onload function should retrieve the data needed to populate the UI



function from_file(file) {
    // adding it to the table
    add_record(file.filename, student.spamprobability, student.actualclass);
  }
  
/**
* @param {String} filename
* @param {Number} spamprobability
* @param {String} actualclass  
*/
function add_record(filename, spamprobability, actualclass) {
 /**
  * the smarter more inclined students might abstract the logic of `from_inputs()` into its own function
  * just like in this file, other than that
  * the students will have to modify their code to accept `Student` objects
  */

 /**
  * the `wrap()` function is optional and most students aren't going to have it
  */
 const data = [filename, spamprobability, actualclass].map((el) => {
   return wrap("td", el);
 });
 const row = wrap("tr", data.join(""));

 /**
  * the students need to append the new row to the tbody element,
  * not the table element
  */
 document.getElementById("chart").getElementsByTagName("tbody")[0].innerHTML +=
   row;
}

/**
 * an optional function which aids in HTML tag wrapping
 * @param {String} tag the HTML tag
 * @param {String} data something to wrap
 * @returns {String} `<tag>data</tag>`
 */
function wrap(tag, data) {
    return `<${tag}>${data}</${tag}>`;
  }


//get data to chart
(function () {
    fetch("http://localhost:8080/spamDetector-1.0/api/spam")
      .then((res) => res.json()) // `.json()` returns a promise, not data
      .then((data) => {
  
        console.log(`Loaded data from ${URL}: `, data);
  
        // using `in` will not work
        for (const file of data["testFiles"]) {
          // just a wrapper around `add_record()`
          from_file(file);
        }
      })
      .catch((err) => {
        console.log("something went wrong: " + err);
      });

    //acurracy
    fetch("http://localhost:8080/spamDetector-1.0/api/spam/accuracy")
    .then((res) => res.json()) // `.json()` returns a promise, not data
    .then((data) => {
        
        document.getElementById("acc").innerHTML = data.val
      
    })
    .catch((err) => {
      console.log("something went wrong: " + err);
    });


    //precision
    //acurracy
    fetch("http://localhost:8080/spamDetector-1.0/api/spam/accuracy")
    .then((res) => res.json()) // `.json()` returns a promise, not data
    .then((data) => {

        document.getElementById("prec").innerHTML = data.val
      
    })
    .catch((err) => {
      console.log("something went wrong: " + err);
    });

  })();
