<?php

require_once ("db.php");
require_once ("state_consituency.php");

header("Content-type: text/html; charset=utf-8");

// Create connection
$conn = new mysqli($servername, $user, $password, $database);

//To handle hindi
mysqli_query($conn, 'SET character_set_results=utf8');
mysqli_query($conn, 'SET names=utf8');

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// get the list of seat/ constituencies
foreach ($all_MPs as $array) {
    $constituency = $array[3];
    $url = "https://myneta.info/api/ver4.1/getDataLS2019BasicDetails.php?message=" . urlencode($constituency) . "&apikey=" . $ADR_key;
    // save the json
    file_put_contents($constituency . ".json", fopen($url, 'r'));

    // parse the json for candidate details - filter them into good/bad buckets (arrays)
    $file_name = file_get_contents($constituency . ".json");
    $json = json_decode($file_name, true);
    $total = 0;

    // for every seat, check json is not null
    if (empty($json) == false) {
        foreach ($json as $candidate) {
            foreach ($candidate as $candidate_data) {
                ++$i;
                if ($i === 1) {
                    $name = $candidate_data;
                } else if ($i === 2) {
                    $constituency_name = $constituency;
                    $constituency_name_hindi = $array[6];
                } else if ($i === 3) {
                    $party = $candidate_data;
                } else if ($i === 4) {
                    $state = $candidate_data;
                    $state_name_hindi = $array[1];
                } else if ($i === 5) {
                    $date_of_election = $candidate_data;
                } else if ($i === 6) {
                    $sex = $candidate_data;
                } else if ($i === 7) {
                    $age = $candidate_data;
                } else if ($i === 8) {
                    $serious_ipc_counts = $candidate_data;
                } else if ($i === 9) {
                    $cases = $candidate_data;
                } else if ($i === 10) {
                    $education = $candidate_data;
                } else if ($i === 11) {
                    $total_assets = $candidate_data;
                } else if ($i === 12) {
                    $movable_assets = $candidate_data;
                } else if ($i === 13) {
                    $immovable_assets = $candidate_data;
                } else if ($i === 14) {
                    $liabilities = $candidate_data;
                } else if ($i === 15) {
                    $pan_given = $candidate_data;
                } else if ($i === 16) {
                    $self_income = $candidate_data;
                } else if ($i === 17) {
                    $total_income = $candidate_data;
                } else if ($i === 18) {
                    $self_profession = $candidate_data;
                } else if ($i === 19) {
                    $position = $candidate_data;
                } else if ($i === 20) {
                    $url = $candidate_data;
                }
            }
            ++$total;
        } // json parsing over
        //echo $constituency_name_hindi;
        //echo $state_name_hindi;
        // check if case of NEW(insert) or CORRECTION(update)
        $sql = "SELECT * FROM loksabha_2019 WHERE constituency = '$constituency' AND name='$name'";
        $result = mysqli_query($conn, $sql) or trigger_error("Query Failed! SQL: $sql - Error: " . mysqli_error(), E_USER_ERROR);
        if (mysqli_num_rows($result) > 0) {
            echo "$constituency exists \n";

            $sql = mysqli_query($conn, "UPDATE loksabha_2019 SET party = '$party',  date_of_election = '$date_of_election', sex = '$sex',  age = '$age', "
                    . "serious_ipc_counts = '$serious_ipc_counts',  cases = '$cases', education = '$education',  total_assets = '$total_assets', "
                    . "movable_assets = '$movable_assets',  immovable_assets = '$immovable_assets', liabilities = '$liabilities',  pan_given = '$pan_given',"
                    . "self_income = '$self_income',  total_income = '$total_income', self_profession = '$self_profession',  position = '$position', "
                    . "url = '$url',  hindi_state = '$state_name_hindi', hindi_constituency = '$constituency_name_hindi'"
                    . "WHERE constituency = '$constituency' AND name='$name'");

            if (!$sql)
                die('Error:' . mysql_error());
        } else {
            $sql = "INSERT INTO loksabha_2019 (name, constituency, party, state, date_of_election, sex, age, serious_ipc_counts,
								cases, education, total_assets, movable_assets, immovable_assets, liabilities, pan_given,
								self_income, total_income, self_profession, position, url, hindi_state, hindi_constituency)
				VALUES ('$name', '$constituency_name', '$party', '$state', '$date_of_election', '$sex', '$age', '$serious_ipc_counts',
								'$cases', '$education', '$total_assets', '$movable_assets', '$immovable_assets', '$liabilities', '$pan_given',
								'$self_income', '$total_income', '$self_profession', '$position', '$url', '$state_name_hindi', '$constituency_name_hindi')";
        }

        if ($conn->query($sql) === TRUE) {
            //nothing to do
        } else {
            echo "Error: " . $sql . "<br>" . $conn->error;
        }

        // Filter Congress and BJP
        $sql = "SELECT name, party FROM loksabha_2019 WHERE constituency = '$constituency' and bucket = ''";
        $result = mysqli_query($conn, $sql) or die('Error:' . mysql_error());
        if (mysqli_num_rows($result) > 0) {
            while ($row = mysqli_fetch_assoc($result)) {
                if ($row[party] === "BJP" || $row[party] === "INC") {
                    $sql = mysqli_query($conn, "UPDATE loksabha_2019 SET bucket = 'illegal-foriegn-funding' WHERE constituency = '$constituency' AND name = '$row[name]'");
                    if (!$sql)
                        die('Error:' . mysql_error());
                }
            }
        }

        // Filter criminals
        $sql = "SELECT name, serious_ipc_counts FROM loksabha_2019 WHERE constituency = '$constituency' and bucket = ''";
        $result = mysqli_query($conn, $sql) or die('Error:' . mysql_error());
        if (mysqli_num_rows($result) > 0) {
            while ($row = mysqli_fetch_assoc($result)) {
                if ($row[serious_ipc_counts] > 0) {
                    $sql = mysqli_query($conn, "UPDATE loksabha_2019 SET bucket = 'criminal cases' WHERE constituency = '$constituency' AND name = '$row[name]'");
                    if (!$sql)
                        die('Error:' . mysql_error());
                }
            }
        }

        // Filter aged-persons > 64
        $sql = "SELECT name, age FROM loksabha_2019 WHERE constituency = '$constituency' and bucket = ''";
        $result = mysqli_query($conn, $sql) or die('Error:' . mysql_error());
        if (mysqli_num_rows($result) > 0) {
            while ($row = mysqli_fetch_assoc($result)) {
                if ($row[age] > 64) {
                    $sql = mysqli_query($conn, "UPDATE loksabha_2019 SET bucket = 'aged' WHERE constituency = '$constituency' AND name = '$row[name]'");
                    if (!$sql)
                        die('Error:' . mysql_error());
                }
            }
        }

        // Filter education < Graduate
        $sql = "SELECT name, education FROM loksabha_2019 WHERE constituency = '$constituency' and bucket = ''";
        $result = mysqli_query($conn, $sql) or die('Error:' . mysql_error());
        if (mysqli_num_rows($result) > 0) {
            while ($row = mysqli_fetch_assoc($result)) {
                if ($row[education] === "Illiterate" || $row[education] === "5th Pass" || $row[education] === "8th Pass" ||
                        $row[education] === "10th Pass" || $row[education] === "12th Pass") {
                    $sql = mysqli_query($conn, "UPDATE loksabha_2019 SET bucket = 'Not graduate' WHERE constituency = '$constituency' AND name = '$row[name]'");
                    if (!$sql)
                        die('Error:' . mysql_error());
                }
            }
        }

        // java array generation
        $sql = "SELECT constituency, hindi_state, hindi_constituency, hindi_name, hindi_party FROM loksabha_2019 where constituency = '$constituency'";

        $result = mysqli_query($conn, $sql) or trigger_error("Query Failed! SQL: $sql - Error: " . mysqli_error(), E_USER_ERROR);
        if (mysqli_num_rows($result) > 0) {
            while ($row = mysqli_fetch_assoc($result)) {
                //echo $row["constituency"] . $row["hindi_state"] . $row["hindi_constituency"] . $row["hindi_name"] . $row["hindi_party"] . "\n";
            }
        }
    }
    echo "Constituency: " . $constituency . ", Total: " . $total . "\n";
}
