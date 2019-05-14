<?php

require_once ("db.php");
require_once ("state_consituency.php");

// Create connection
$conn = new mysqli($servername, $user, $password, $database);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

//To handle hindi
mysqli_query($conn, "SET character_set_results=utf8");
mysqli_query($conn, "SET names=utf8");
mysqli_query($conn, "SET character_set_client=utf8");
mysqli_query($conn, "SET character_set_connection=utf8");
mysqli_query($conn, "SET collation_connection=utf8_general_ci");


// Write state name and constituency name in HINDI to DB
foreach ($all_MPs as $array) {
    //echo "state = $array[0], constituency = $array[3], hindi_state = $array[1], hindi_constituency = $array[6]\n";
    $sql = "UPDATE loksabha_2019 SET hindi_state = '$array[1]', hindi_constituency = '$array[6]' "
            . "WHERE state = '$array[0]' AND constituency = '$array[3]' AND hindi_state = '' AND hindi_constituency = ''";
    $result = mysqli_query($conn, $sql) or die('Error:' . mysqli_error($conn));
}
echo "Done: State name and Constituency name in HINDI to DB\n";

// Write full form of party to DB
foreach ($short_form_party as $array) {
    //echo "full party = $array[1], short party = $array[0]\n";
    $sql = "UPDATE loksabha_2019 SET party = '$array[1]' WHERE party = '$array[0]'";
    $result = mysqli_query($conn, $sql) or die('Error:' . mysqli_error($conn));
}
echo "Done: Full form of party to DB\n";

// Write party name in HINDI to DB
$sql = "SELECT DISTINCT party FROM loksabha_2019 WHERE hindi_party = ''";
$result = mysqli_query($conn, $sql) or die('Error:' . mysqli_error($conn));
if (mysqli_num_rows($result) > 0) {
    while ($row = mysqli_fetch_assoc($result)) {
        $hindi_party = get_hindi($row[party]);
        $sql = "UPDATE loksabha_2019 SET hindi_party = '$hindi_party' WHERE party = '$row[party]' AND hindi_party = ''";
        $execute = mysqli_query($conn, $sql) or die('Error:' . mysqli_error($conn));
    }
}
$sql = "UPDATE loksabha_2019 SET hindi_party = 'निर्दलीय' WHERE party = 'Independent'";
$result = mysqli_query($conn, $sql) or die('Error:' . mysqli_error($conn));
echo "Done: Party name in HINDI to DB\n";

// Write candidate name in HINDI to DB
$sql = "SELECT id, name FROM loksabha_2019 WHERE hindi_name = ''";
$result = mysqli_query($conn, $sql) or die('Error:' . mysqli_error($conn));
if (mysqli_num_rows($result) > 0) {
    while ($row = mysqli_fetch_assoc($result)) {
        $hindi_name = get_hindi($row[name]);
        //echo "name = $row[name], hindi_name = $hindi_name\n";
        $sql = "UPDATE loksabha_2019 SET hindi_name = '$hindi_name' WHERE id = '$row[id]' AND name = '$row[name]' AND hindi_name = ''";
        $execute = mysqli_query($conn, $sql) or die('Error:' . mysqli_error($conn));
    }
}
echo "Done: Candidate name in HINDI to DB\n";

function get_hindi($english_name) {
    global $GOOGLE_key;
    $url = 'https://www.googleapis.com/language/translate/v2?key=' . $GOOGLE_key . '&q=' . rawurlencode($english_name) . '&source=en&target=hi';
    $handle = curl_init($url);
    curl_setopt($handle, CURLOPT_RETURNTRANSFER, true);
    $response = curl_exec($handle);
    echo "Respone: $response";
    if ($response) {
        $responseDecoded = json_decode($response, true);
        curl_close($handle);
        $hindi_name = $responseDecoded['data']['translations'][0]['translatedText'];
        return $hindi_name;
    }
    return " ";
}
