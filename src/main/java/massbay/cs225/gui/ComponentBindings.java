package massbay.cs225.gui;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import massbay.cs225.api.Nameable;

import java.lang.ref.WeakReference;

public final class ComponentBindings {
    private ComponentBindings() {
        throw new UnsupportedOperationException();
    }

    public static BooleanBinding isRenameable(ObservableValue<? extends Nameable> component) {
        return new BooleanBinding() {
            {
                bind(component);
            }

            @Override
            protected boolean computeValue() {
                return component.getValue() != null && component.getValue().isRenameable();
            }

            @Override
            public void dispose() {
                unbind(component);

                super.dispose();
            }

            @Override
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(component);
            }
        };
    }

    public static StringBinding name(ObservableValue<? extends Nameable> component) {
        return new StringBinding() {
            private WeakReference<StringProperty> name;
            private ObservableList<Object> dependencies = FXCollections.observableArrayList();

            {
                bind(component);
                dependencies.add(component);

                if (component.getValue() == null) {
                    name = new WeakReference<>(null);
                } else {
                    StringProperty name = component.getValue().nameProperty();
                    this.name = new WeakReference<>(name);
                    bind(name);
                    dependencies.add(this.name);
                }
            }

            @Override
            protected void onInvalidating() {
                StringProperty name = this.name == null ? null : this.name.get();
                Nameable value = component.getValue();

                if (name != value) {
                    if (name != null) {
                        unbind(name);
                        dependencies.remove(this.name);
                    }

                    if (value == null) {
                        this.name = new WeakReference<>(null);
                    } else {
                        name = value.nameProperty();
                        this.name = new WeakReference<>(name);

                        if (name != null) {
                            bind(name);
                            dependencies.add(this.name);
                        }
                    }
                }

                super.onInvalidating();
            }

            @Override
            protected String computeValue() {
                Nameable value = component.getValue();
                return value == null ? null : value.getName();
            }

            @Override
            public void dispose() {
                StringProperty name = getName();

                if (name != null) {
                    unbind(name);
                    dependencies.remove(this.name);
                }

                unbind(component);
                dependencies.remove(component);

                super.dispose();
            }

            private StringProperty getName() {
                StringProperty name = this.name == null ? null : this.name.get();
                Nameable value = component.getValue();

                if (name == null && value != null) {
                    name = value.nameProperty();
                    this.name = new WeakReference<>(name);

                    bind(name);
                    dependencies.add(this.name);
                }

                return name;
            }

            @SuppressWarnings("unchecked")
            @Override
            public ObservableList<?> getDependencies() {
                return dependencies;
            }
        };
    }
}
