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

// java array generation
$green_bucket = "SELECT state, id, constituency, name, party, total_assets, url, hindi_state, hindi_constituency, hindi_name, hindi_party "
        . "FROM loksabha_2019 WHERE bucket = '' ORDER BY loksabha_2019.state ASC, loksabha_2019.constituency ASC";
$result_green_bucket = mysqli_query($conn, $green_bucket) or die('Error:' . mysqli_error($conn));
$prev_state = '';
$tmp = '';
if (mysqli_num_rows($result_green_bucket) > 0) {
    while ($row = mysqli_fetch_assoc($result_green_bucket)) {
        if ($prev_state != $row[state]) {
            $prev_state = $row[state];
            // state name change
            if ($tmp != '')
                error_log("};\n\n", 3, $FILE);

            // The file name with & creates problems, so replacing it. JM app couldn't read as valid Java file.
            $tmp = str_replace(" ", "_", $prev_state);
            $tmp = str_replace("&", "and", $tmp);

            // path to android source file name
            $FILE = "../app/src/main/java/in/filternet/jantamalik/LokSabha_Election_2019/" . $tmp . ".java";
            //echo "State : $tmp\n";
            unlink($FILE);

            error_log("/* Auto-generated file from db2java.php*/
package in.jantamalik.LokSabha_Election_2019;

public class " . str_replace(" ", "_", $tmp) . " {\n", 3, $FILE);

            error_log("public static final String[][] green_bucket = {\n", 3, $FILE);
        }
        error_log("{\"$row[state]\", \"$row[constituency]\", \"$row[name]\", \"$row[party]\", \"$row[total_assets]\", \"$row[url]\",  "
                . "\"$row[hindi_state]\",  \"$row[hindi_constituency]\", \"$row[hindi_name]\", \"$row[hindi_party]\"}, \n", 3, $FILE);
    }
}
error_log("};\n\n", 3, $FILE);

$red_bucket = "SELECT state, id, constituency, name, bucket, hindi_state, hindi_constituency, hindi_name "
        . "FROM loksabha_2019 WHERE bucket != '' ORDER BY loksabha_2019.state ASC, loksabha_2019.constituency ASC";
$result_red_bucket = mysqli_query($conn, $red_bucket) or die('Error:' . mysqli_error($conn));
$prev_state = '';
$tmp = '';
if (mysqli_num_rows($result_red_bucket) > 0) {
    while ($row = mysqli_fetch_assoc($result_red_bucket)) {
        if ($prev_state != $row[state]) {
            $prev_state = $row[state];
            // state name change
            if ($tmp != '') {
                error_log("};\n\n", 3, $FILE);
                //java class closing bracket when State != West Bengal
                error_log("}", 3, $FILE);
            }

            // The file name with & creates problems, so replacing it. JM app couldn't read as valid Java file.
            $tmp = str_replace(" ", "_", $prev_state);
            $tmp = str_replace("&", "and", $tmp);

            // path to android source file name
            $FILE = "../app/src/main/java/in/filternet/jantamalik/LokSabha_Election_2019/" . $tmp . ".java";

            error_log("public static final String[][] red_bucket = {\n", 3, $FILE);
        }
        error_log("{ \"$row[state]\", \"$row[constituency]\", \"$row[name]\", \"$row[bucket]\",  \" \","
                . "\"$row[hindi_state]\",  \"$row[hindi_constituency]\", \"$row[hindi_name]\", \" \"}, \n", 3, $FILE);
    }
}

error_log("};\n\n", 3, $FILE);
//java class closing bracket when State = West Bengal
error_log("}", 3, $FILE);
