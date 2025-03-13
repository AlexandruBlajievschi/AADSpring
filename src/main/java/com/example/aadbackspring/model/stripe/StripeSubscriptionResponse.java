package com.example.aadbackspring.model.stripe;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StripeSubscriptionResponse {
    private String stripeCustomerId;
    private String stripeSubscriptionId;
    private String stripePaymentMethodId;
    private String username;

    public StripeSubscriptionResponse() {
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getStripeSubscriptionId() {
        return stripeSubscriptionId;
    }

    public void setStripeSubscriptionId(String stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    public String getStripePaymentMethodId() {
        return stripePaymentMethodId;
    }

    public void setStripePaymentMethodId(String stripePaymentMethodId) {
        this.stripePaymentMethodId = stripePaymentMethodId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Builder pattern implementation
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String stripeCustomerId;
        private String stripeSubscriptionId;
        private String stripePaymentMethodId;
        private String username;

        public Builder stripeCustomerId(String stripeCustomerId) {
            this.stripeCustomerId = stripeCustomerId;
            return this;
        }

        public Builder stripeSubscriptionId(String stripeSubscriptionId) {
            this.stripeSubscriptionId = stripeSubscriptionId;
            return this;
        }

        public Builder stripePaymentMethodId(String stripePaymentMethodId) {
            this.stripePaymentMethodId = stripePaymentMethodId;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public StripeSubscriptionResponse build() {
            StripeSubscriptionResponse response = new StripeSubscriptionResponse();
            response.setStripeCustomerId(this.stripeCustomerId);
            response.setStripeSubscriptionId(this.stripeSubscriptionId);
            response.setStripePaymentMethodId(this.stripePaymentMethodId);
            response.setUsername(this.username);
            return response;
        }
    }
}
