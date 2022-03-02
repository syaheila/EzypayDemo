# Simple spring demos

project is based on assessment shared with use case below
"Your team is building a recurring payment system and you’re tasked with building the subscription module. A subscription tells the system when to bill a customer and includes all the details needed to create an invoice."

Input:
▪ the amount to charge per invoice, ie $10.00
▪ the subscription type – DAILY, WEEKLY or MONTHLY.
▪ day of week/month – if the subscription type was WEEKLY, the day of the week - i.e. TUESDAY. If it was MONTHLY, the date of the month - i.e. 20
▪ The start and end date of the entire subscription, with a maximum duration of 3 months

Output:
▪ Amount – the amount entered
▪ Subscription type – the subscription type entered
▪ Invoice dates – all the dates in ‘dd/MM/yyyy’ format that invoices will be issued on. For example, a weekly subscription every Tuesday from the 6th of Feb 2018 to the 27th of Feb 2018 will have the following invoice dates: ["06/02/2018", "13/02/2018", "20/02/2018", "27/02/2018"]

to run: (requires JDK1.7) java -jar SpringSubscriptionService-0.0.1-SNAPSHOT.jar
to test: (using tool eg:postman) 
  method: POST
  URL: http://localhost:8080/subscription/invoice?key=SHARED_KEY
  Headers: Content-Type:application/json
  sample input: {
                    "amount":22,
                    "subsType":"WEEKLY",
                    "subsDay": "THURSDAY",
                    "startDate":"23/02/2022",
                    "endDate":"01/04/2022"
                }
