Create a simple RESTful software service that will allow a merchant to create a new simple offer.
Offers, once created, may be queried.
After the period of time defined on the offer it should expire and
further requests to query the offer should reflect that somehow.
Before an offer has expired users may cancel it.

### Assumptions

- Offers created started from now by default;
- Money amounts specified in minor units;
- Allowed currencies are: EUR, GBP, USD;
- When expired/cancelled/not-started offer queried, the HTTP status is "410 Gone"

### Usage:

Create a new offer with:

    curl -v -XPOST -H'Content-Type: application/json' \
        -d'{"description": "Hi!", "amountInMinorUnits": "100", "currency": "USD", "durationInSeconds": "300"}' \
        localhost:8080/offers

Example expected response:

    Created: 71b253e7-1a8f-4626-8369-d9765e130200
    
Retrieve created offer with:

    curl localhost:8080/offers/71b253e7-1a8f-4626-8369-d9765e130200
    
Example expected response:

    {
      "offerId": "71b253e7-1a8f-4626-8369-d9765e130200",
      "description": "Hi!",
      "currency": "USD",
      "amountInMinorUnits": 100,
      "start": "2020-09-20T11:54:36.268839292Z",
      "durationInSeconds": 300,
      "expired": false,
      "cancelled": false,
      "active": true
    }

Cancel offer with:

    curl -XDELETE localhost:8080/offers/71b253e7-1a8f-4626-8369-d9765e130200

