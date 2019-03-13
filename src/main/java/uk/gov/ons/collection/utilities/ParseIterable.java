package uk.gov.ons.collection.utilities;

import uk.gov.ons.collection.entity.ValidationEntity;

import java.util.ArrayList;
import java.util.List;

public class ParseIterable {

    public <T> List<T> parseIterable(Iterable<T> iterable){
        List<T> target = new ArrayList<>();
        iterable.forEach(element -> target.add(element));
        return target;
    }

    public class keyConstraintViolation extends Exception{
        public keyConstraintViolation(String error){
            super(error);
        }
    }
}
