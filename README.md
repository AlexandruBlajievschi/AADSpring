# SpringBoot Backend

## Running the Application Locally

### Prerequisites
Before running the project, make sure you have the following installed:

1. **Docker**: [Install Docker](https://docs.docker.com/get-docker/)
2. **Docker Compose**: [Install Docker Compose](https://docs.docker.com/compose/install/)

# First Approach

1. In order for you to test the Stripe payment system you will have to configure your Stripe account, after that to find your STRIPE WEBHOOK SECRET.
2. Replace the actual value of *STRIPE_WEBHOOK_SECRET* in .env file with your value from your account.
3. Go to the root of this folder and run *docker-compose up --build* command. Wait for everything to be built.
4. Download Stripe CLI and configure it using the instruction here: [Stripe CLI](https://docs.stripe.com/stripe-cli?install-method=windows)
5. Run the "stripe listen --forward-to http://localhost:8080" command.
6. Everything is set up. You can now test our backend fully. Moreover, the Stripe external payment now can be tested using our backend.
7. OpenAPI specification can be found in "openapi_spec.pdf" document.


# Second Approach


1. **Clone the repository:**

   ```bash
   git clone https://github.com/AlexandruBlajievschi/AADSpring.git
   cd AADSpring

2. Create .env file at the root of the directory with the following variables:
   <p> CMC_API_KEY=bab4713c-03bd-4f18-b7f1-ea27196f7cb7 </p>
   <p> DATASOURCE_PASSWORD=AlexRaph07 </p>
   <p> DATASOURCE_URL=jdbc:postgresql://db:5432/SpringDB </p>
   <p> DATASOURCE_USERNAME=postgres </p>
   <p> JWT_EXPIRATION=8640000 </p>
   <p> JWT_SECRET=YourSuperSecretKeyChangeThis </p>
   <p> STRIPE_SECRET_KEY=sk_test_51R1TfJ4gHyUjWEHaYoQvmiBS1UiCKgorEdcg3SJjUMmKR3z3hroIpxTNf6sUk75Wij5jPKU18Bkw91G6Q7f5iROG00XkBnIYx9  </p>
   <p> STRIPE_WEBHOOK_SECRET=whsec_1c9236d3523e41e9b8c0a11d6cc9a358e07755aceb6cdc39ef939549ca1b45a4  </p>
   <p> ADMIN_EMAIL=admin@example.com </p>
   <p> ADMIN_PASSWORD=super123* </p>
   <p> DEFAULT_USER_EMAIL=user@example.com </p>
   <p> DEFAULT_USER_PASSWORD=user123* </p>

3. From here repeat the first approach's instructions.

P.S: All the credentials are test ones for you to be able to test the application.