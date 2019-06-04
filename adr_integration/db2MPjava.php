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
$FILE = "../app/src/main/java/in/filternet/jantamalik/Kendra/MPdata.java";
//echo "State : $tmp\n";
unlink($FILE);

error_log("/* Auto-generated file from db2MPjava.php*/

package in.filternet.jantamalik.Kendra;

public final class MPdata {

\tpublic static final String[][] all_MPs = {\n\n", 3, $FILE);

// java array generation
$sql = "SELECT state, constituency, name, hindi_state, hindi_constituency, hindi_name, phone, phone2, phone3, email, email2, address "
        . "FROM loksabha_2019 WHERE elected = 1 ORDER BY loksabha_2019.state ASC, loksabha_2019.constituency ASC";
$result = mysqli_query($conn, $sql) or die('Error:' . mysqli_error($conn));

if (mysqli_num_rows($result) > 0) {
    while ($row = mysqli_fetch_assoc($result)) {
        error_log("\t\t{\"$row[state]\", \"$row[constituency]\", \"$row[name]\", \"$row[hindi_state]\", \"$row[hindi_constituency]\", \"$row[hindi_name]\",  "
                . "\"$row[phone]\",  \"$row[phone2]\", \"$row[phone3]\", \"$row[email]\", \"$row[email2]\", \"$row[address]\"}, \n", 3, $FILE);
    }
}

error_log("\n\t};\n\n", 3, $FILE);
//java class closing bracket when State = West Bengal
error_log("}", 3, $FILE);
