package hu.cubix.hr.gergelynemeth.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "hr.employees.salary")
@Component
public class SalaryConfigurationProperties {

    private Limit limit;
    private Percentage percentage;

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    public Percentage getPercentage() {
        return percentage;
    }

    public void setPercentage(Percentage percentage) {
        this.percentage = percentage;
    }

    public static class Limit {
        private float firstCategory;
        private float secondCategory;
        private float thirdCategory;

        public float getFirstCategory() {
            return firstCategory;
        }

        public void setFirstCategory(int firstCategory) {
            this.firstCategory = firstCategory;
        }

        public float getSecondCategory() {
            return secondCategory;
        }

        public void setSecondCategory(int secondCategory) {
            this.secondCategory = secondCategory;
        }

        public float getThirdCategory() {
            return thirdCategory;
        }

        public void setThirdCategory(int thirdCategory) {
            this.thirdCategory = thirdCategory;
        }
    }

    public static class Percentage {
        private int firstCategory;
        private int secondCategory;
        private int thirdCategory;
        private int fourthCategory;

        public int getFirstCategory() {
            return firstCategory;
        }

        public void setFirstCategory(int firstCategory) {
            this.firstCategory = firstCategory;
        }

        public int getSecondCategory() {
            return secondCategory;
        }

        public void setSecondCategory(int secondCategory) {
            this.secondCategory = secondCategory;
        }

        public int getThirdCategory() {
            return thirdCategory;
        }

        public void setThirdCategory(int thirdCategory) {
            this.thirdCategory = thirdCategory;
        }

        public int getFourthCategory() {
            return fourthCategory;
        }

        public void setFourthCategory(int fourthCategory) {
            this.fourthCategory = fourthCategory;
        }
    }
}
