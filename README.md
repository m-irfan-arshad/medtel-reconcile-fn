# reconcile-fn

<!-- demo pub/sub trigger commands -->

curl -X DELETE -H "Authorization: Bearer $(gcloud auth application-default print-access-token)" https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/finalFhirStore/fhir/Patient/[ID HERE]

<!--  -->

curl -X POST -H "Authorization: Bearer $(gcloud auth application-default print-access-token)" -H "Content-Type: application/fhir+json; charset=utf-8" --data @appointment1.json "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/fhirstore/fhir/Appointment"

curl -X POST -H "Authorization: Bearer $(gcloud auth application-default print-access-token)" -H "Content-Type: application/fhir+json; charset=utf-8" --data @appointment2.json "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/fhirstore/fhir/Appointment"

<!--  -->

curl -X POST -H "Authorization: Bearer $(gcloud auth application-default print-access-token)" -H "Content-Type: application/fhir+json; charset=utf-8" --data @patient1.json "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/fhirstore/fhir/Patient"

curl -X POST -H "Authorization: Bearer $(gcloud auth application-default print-access-token)" -H "Content-Type: application/fhir+json; charset=utf-8" --data @patient2.json "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/fhirstore/fhir/Patient"


<!--  -->

curl -X POST -H "Authorization: Bearer $(gcloud auth application-default print-access-token)" -H "Content-Type: application/fhir+json; charset=utf-8" --data @encounter1.json "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/fhirstore/fhir/Encounter"

curl -X POST -H "Authorization: Bearer $(gcloud auth application-default print-access-token)" -H "Content-Type: application/fhir+json; charset=utf-8" --data @encounter2.json "https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/fhirstore/fhir/Encounter"


