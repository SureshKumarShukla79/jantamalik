<?php

require_once ("db.php");

// Create connection
$conn = new mysqli($servername, $user, $password, $database);

//To handle hindi
mysqli_query($conn, 'SET character_set_results=utf8');
mysqli_query($conn, 'SET names=utf8');

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// path to android source file name
$FILE = "../app/src/main/java/in/filternet/jantamalik/Election_2019.java";
unlink($FILE);

//java class opening bracket
error_log("package in.filternet.jantamalik;

public class Election_2019 {
public static final String[][] green_bucket = {\n", 3, $FILE);

// java array generation
$green_bucket = "SELECT state, id, constituency, name, party, url, hindi_state, hindi_constituency, hindi_name, hindi_party, hindi_research FROM loksabha_2019 WHERE bucket = ''";
$result_green_bucket = mysqli_query($conn, $green_bucket) or die('Error:' . mysqli_error($conn));
if (mysqli_num_rows($result_green_bucket) > 0) {
    while ($row = mysqli_fetch_assoc($result_green_bucket)) {
        error_log("{ \"$row[state]\",  \"$row[id]\", \"$row[constituency]\", \"$row[name]\", \"$row[party]\", \"$row[url]\",  "
                . "\"$row[hindi_state]\",  \"$row[hindi_constituency]\", \"$row[hindi_name]\", \"$row[hindi_party]\", \"$row[hindi_research]\"}, \n", 3, $FILE);
    }
}
error_log("}; \n\n", 3, $FILE);

error_log("public static final String[][] red_bucket = {\n", 3, $FILE);
$red_bucket = "SELECT state, id, constituency, name, bucket, hindi_state, hindi_constituency, hindi_name FROM loksabha_2019 WHERE bucket != ''";
$result_red_bucket = mysqli_query($conn, $red_bucket) or die('Error:' . mysqli_error($conn));
if (mysqli_num_rows($result_red_bucket) > 0) {
    while ($row = mysqli_fetch_assoc($result_red_bucket)) {
        error_log("{ \"$row[state]\",  \"$row[id]\",  \"$row[constituency]\", \"$row[name]\", \"$row[bucket]\",  \" \","
                . "\"$row[hindi_state]\",  \"$row[hindi_constituency]\", \"$row[hindi_name]\", \" \"}, \n", 3, $FILE);
    }
}
error_log("}; \n\n", 3, $FILE);

//java class closing bracket
error_log("}", 3, $FILE);
