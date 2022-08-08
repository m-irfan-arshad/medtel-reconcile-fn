# reconcile-fn

demo pub/sub trigger commands

----

curl -X DELETE -H "Authorization: Bearer $(gcloud auth application-default print-access-token)" https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/finalFhirStore/fhir/Patient/[ID HERE]

----
curl -X POST -H "Authorization: Bearer $(gcloud auth application-default print-access-token)" -H "Content-Type: application/fhir+json; charset=utf-8" --data @appointment1.json "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-east4/datasets/datastore/fhirStores/fhirstore/fhir/Appointment"

curl -X POST -H "Authorization: Bearer $(gcloud auth application-default print-access-token)" -H "Content-Type: application/fhir+json; charset=utf-8" --data @appointment2.json "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/fhirstore/fhir/Appointment"

----

curl -X POST -H "Authorization: Bearer $(gcloud auth application-default print-access-token)" -H "Content-Type: application/fhir+json; charset=utf-8" --data @patient1.json "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-east4/datasets/datastore/fhirStores/fhirstore/fhir/Patient"

curl -X POST -H "Authorization: Bearer $(gcloud auth application-default print-access-token)" -H "Content-Type: application/fhir+json; charset=utf-8" --data @patient2.json "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/fhirstore/fhir/Patient"

----

curl -X POST -H "Authorization: Bearer $(gcloud auth application-default print-access-token)" -H "Content-Type: application/fhir+json; charset=utf-8" --data @encounter1.json "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-east4/datasets/datastore/fhirStores/fhirstore/fhir/Encounter"

curl -X POST -H "Authorization: Bearer $(gcloud auth application-default print-access-token)" -H "Content-Type: application/fhir+json; charset=utf-8" --data @encounter2.json "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/fhirstore/fhir/Encounter"

----

curl -X GET -H "Authorization: Bearer $(gcloud auth application-default print-access-token)" -H "Content-Type: application/fhir+json; charset=utf-8" "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/finalFhirStore/fhir/ServiceRequest/06666ce7-9f2d-4325-5555-df4d5040a144"




curl -X PUT -H "Authorization: Bearer $(gcloud auth application-default print-access-token)" -H "Content-Type: application/fhir+json; charset=utf-8" --data @service-request1.json "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/finalFhirStore/fhir/ServiceRequest/1e03ea4b-0cb0-dc43-a384-5bf274eb86ba"



curl -X PUT -H "Authorization: Bearer $(gcloud auth application-default print-access-token)" -H "Content-Type: application/fhir+json; charset=utf-8" --data @test-appointment.json "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/finalFhirStore/fhir/Appointment/4ac0cd7d-2ee7-f837-18e5-c1445713f6fa"

curl -X PUT -H "Authorization: Bearer $(gcloud auth application-default print-access-token)" -H "Content-Type: application/fhir+json; charset=utf-8" --data @test-practitioner-role.json "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/finalFhirStore/fhir/PractitionerRole/f25f6cb4-cf6d-c1fc-529c-3a90317750bc"

curl -X PUT -H "Authorization: Bearer $(gcloud auth application-default print-access-token)" -H "Content-Type: application/fhir+json; charset=utf-8" --data @test-practitioner.json "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/finalFhirStore/fhir/Practitioner/7a5fc2e9-c8dd-de63-d0c4-6c66e9c9bf01"





{
    "resourceType": "Appointment",
    "id": "4ac0cd7d-2ee7-f837-18e5-c1445713f6fa",
    "identifier": [
        {
            "value": "2767",
            "type": {
                "coding": [
                    {
                        "code": "FILL",
                        "display": "Filler Identifier",
                        "system": "http://terminology.hl7.org/CodeSystem/v2-0203"
                    }
                ]
            }
        }
    ],
    "start": "2022-04-29T11:10:00Z",
    "end": "2022-04-29T11:10:00Z",
    "status": "booked",
    "supportingInformation": [
        {
            "reference": "PractitionerRole/f25f6cb4-cf6d-c1fc-529c-3a90317750bc"
        }
    ],
    "participant": [
        {
            "actor": {
                "reference": "Patient/27cbc4c3-95e6-194a-ab62-2d07dbfbd458"
            },
            "status": "accepted"
        },
        {
            "actor": {
                "reference": "Location/ce692c7c-84ea-4b8f-d3ff-3ec9f0e51bd8"
            },
            "status": "tentative"
        },
        {
            "actor": {
                "reference": "Practitioner/bf0b9fec-b7f0-e253-7671-5fe1ae23dc02"
            },
            "type": [
                {
                "coding": [
                    {
                    "code": "1.1",
                    "display": "Primary"
                    }
                ]
                }
            ],
            "period": {
                "start": "2022-04-29T11:10:00Z"
            },
            "status": "tentative"
        },
        {
            "type": [
                {
                    "coding": [
                        {
                            "code": "2.10",
                            "display": "Anesthesiologist"
                        }
                    ]
                }
            ],
            "period": {
                "start": "2022-04-29T11:10:00Z"
            },
            "status": "tentative"
        },
        {
            "type": [
                {
                    "coding": [
                        {
                            "code": "2.20",
                            "display": "CRNA"
                        }
                    ]
                }
            ],
            "period": {
                "start": "2022-04-29T11:10:00Z"
            },
            "status": "tentative"
        }
    ],
    "basedOn": [
        {
            "reference": "ServiceRequest/1e03ea4b-0cb0-dc43-a384-5bf274eb86ba"
        }
    ]
}