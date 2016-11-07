package tds.config;

public class Accommodation {
    private int valueCount;
    private int segmentPosition;
    private boolean disableOnGuestSession;
    private int toolTypeSortOrder;
    private int toolValueSortOrder;
    private String toolMode;
    private String accType;
    private String accValue;
    private String accCode;
    private boolean defaultAccommodation;
    private boolean allowCombine;
    private boolean allowChange;
    private boolean functional;
    private boolean selectable;
    private boolean visible;
    private boolean studentControl;
    private boolean entryControl;
    private String dependsOnToolType;
    private String typeMode;

    public int getValueCount() {
        return valueCount;
    }

    public int getSegmentPosition() {
        return segmentPosition;
    }

    public boolean isDisableOnGuestSession() {
        return disableOnGuestSession;
    }

    public int getToolTypeSortOrder() {
        return toolTypeSortOrder;
    }

    public int getToolValueSortOrder() {
        return toolValueSortOrder;
    }

    public String getToolMode() {
        return toolMode;
    }

    public String getAccType() {
        return accType;
    }

    public String getAccValue() {
        return accValue;
    }

    public String getAccCode() {
        return accCode;
    }

    public boolean isAllowCombine() {
        return allowCombine;
    }

    public boolean isFunctional() {
        return functional;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isStudentControl() {
        return studentControl;
    }

    public boolean isEntryControl() {
        return entryControl;
    }

    public String getDependsOnToolType() {
        return dependsOnToolType;
    }

    public boolean isDefaultAccommodation() {
        return defaultAccommodation;
    }

    public String getTypeMode() {
        return typeMode;
    }

    public boolean isAllowChange() {
        return allowChange;
    }

    public static final class Builder {
        private int valueCount;
        private int segmentPosition;
        private boolean disableOnGuestSession;
        private int toolTypeSortOrder;
        private int toolValueSortOrder;
        private String toolMode;
        private String accType;
        private String accValue;
        private String accCode;
        private boolean allowCombine;
        private boolean functional;
        private boolean selectable;
        private boolean visible;
        private boolean studentControl;
        private boolean entryControl;
        private String dependsOnToolType;
        private boolean defaultAccommodation;
        private String typeMode;
        private boolean allowChange;

        public static Builder anAccommodationa() {
            return new Builder();
        }

        public Builder withValueCount(int valueCount) {
            this.valueCount = valueCount;
            return this;
        }

        public Builder withSegmentPosition(int segmentPosition) {
            this.segmentPosition = segmentPosition;
            return this;
        }

        public Builder withDisableOnGuestSession(boolean disableOnGuestSession) {
            this.disableOnGuestSession = disableOnGuestSession;
            return this;
        }

        public Builder withToolTypeSortOrder(int toolTypeSortOrder) {
            this.toolTypeSortOrder = toolTypeSortOrder;
            return this;
        }

        public Builder withToolValueSortOrder(int toolValueSortOrder) {
            this.toolValueSortOrder = toolValueSortOrder;
            return this;
        }

        public Builder withToolMode(String toolMode) {
            this.toolMode = toolMode;
            return this;
        }

        public Builder withAccType(String accType) {
            this.accType = accType;
            return this;
        }

        public Builder withAccValue(String accValue) {
            this.accValue = accValue;
            return this;
        }

        public Builder withAccCode(String accCode) {
            this.accCode = accCode;
            return this;
        }

        public Builder withAllowCombine(boolean allowCombine) {
            this.allowCombine = allowCombine;
            return this;
        }

        public Builder withFunctional(boolean functional) {
            this.functional = functional;
            return this;
        }

        public Builder withSelectable(boolean selectable) {
            this.selectable = selectable;
            return this;
        }

        public Builder withVisible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public Builder withStudentControl(boolean studentControl) {
            this.studentControl = studentControl;
            return this;
        }

        public Builder withEntryControl(boolean entryControl) {
            this.entryControl = entryControl;
            return this;
        }

        public Builder withDependsOnToolType(String dependsOnToolType) {
            this.dependsOnToolType = dependsOnToolType;
            return this;
        }

        public Builder withDefaultAccommodation(boolean defaultAccommodation) {
            this.defaultAccommodation = defaultAccommodation;
            return this;
        }

        public Builder withTypeMode(String typeMode) {
            this.typeMode = typeMode;
            return this;
        }

        public Builder withAllowChange(boolean allowChange) {
            this.allowChange = allowChange;
            return this;
        }

        public Accommodation build() {
            Accommodation accommodation = new Accommodation();
            accommodation.valueCount = this.valueCount;
            accommodation.toolValueSortOrder = this.toolValueSortOrder;
            accommodation.segmentPosition = this.segmentPosition;
            accommodation.accValue = this.accValue;
            accommodation.selectable = this.selectable;
            accommodation.dependsOnToolType = this.dependsOnToolType;
            accommodation.functional = this.functional;
            accommodation.entryControl = this.entryControl;
            accommodation.studentControl = this.studentControl;
            accommodation.disableOnGuestSession = this.disableOnGuestSession;
            accommodation.toolMode = this.toolMode;
            accommodation.accCode = this.accCode;
            accommodation.toolTypeSortOrder = this.toolTypeSortOrder;
            accommodation.visible = this.visible;
            accommodation.allowCombine = this.allowCombine;
            accommodation.accType = this.accType;
            accommodation.defaultAccommodation = this.defaultAccommodation;
            accommodation.typeMode = this.typeMode;
            accommodation.allowChange = this.allowChange;
            return accommodation;
        }
    }
}
