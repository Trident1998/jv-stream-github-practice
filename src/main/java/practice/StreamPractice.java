package practice;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import model.Candidate;
import model.Cat;
import model.People;

public class StreamPractice {
    /**
     * Given list of strings where each element contains 1+ numbers:
     * input = {"5,30,100", "0,22,7", ...}
     * return min integer value. One more thing - we're interested in even numbers.
     * If there is no needed data throw RuntimeException with message
     * "Can't get min value from list: < Here is our input 'numbers' >"
     */
    public int findMinEvenNumber(List<String> numbers) {
        return numbers.stream()
                .map(string -> string.split(","))
                .flatMap(Arrays::stream)
                .map(Integer::parseInt)
                .mapToInt(number -> number)
                .filter(number -> number % 2 == 0)
                .sorted()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't get min value from list"));
    }

    /**
     * Given a List of Integer numbers,
     * return the average of all odd numbers from the list or throw NoSuchElementException.
     * But before that subtract 1 from each element on an odd position (having the odd index).
     */
    public Double getOddNumsAverage(List<Integer> numbers) {
        return IntStream.range(0, numbers.size())
                .map(i -> i % 2 == 0 ? numbers.get(i) : numbers.get(i) - 1)
                .filter(n -> n % 2 != 0)
                .average()
                .getAsDouble();
    }

    /**
     * Given a List of `People` instances (having `name`, `age` and `sex` fields),
     * for example, `Arrays.asList( new People(«Victor», 16, Sex.MAN),
     * new People(«Helen», 42, Sex.WOMEN))`,
     * select from the List only men whose age is from `fromAge` to `toAge` inclusively.
     * <p>
     * Example: select men who can be recruited to army (from 18 to 27 years old inclusively).
     */
    public List<People> selectMenByAge(List<People> peopleList, int fromAge, int toAge) {
        Predicate<People> selectMenByAge = people -> fromAge < people.getAge()
                && people.getAge() <= toAge
                && people.getSex().equals(People.Sex.MAN);

        return peopleList.stream()
                .filter(selectMenByAge)
                .collect(Collectors.toList());
    }

    /**
     * Given a List of `People` instances (having `name`, `age` and `sex` fields),
     * for example, `Arrays.asList( new People(«Victor», 16, Sex.MAN),
     * new People(«Helen», 42, Sex.WOMEN))`,
     * select from the List only people whose age is from `fromAge` and to `maleToAge` (for men)
     * or to `femaleToAge` (for women) inclusively.
     * <p>
     * Example: select people of working age
     * (from 18 y.o. and to 60 y.o. for men and to 55 y.o. for women inclusively).
     */
    public List<People> getWorkablePeople(int fromAge, int femaleToAge,
                                          int maleToAge, List<People> peopleList) {
        Predicate<People> selectWorkablePeople = people -> fromAge <= people.getAge()
                && people.getAge() <= maleToAge
                && people.getSex().equals(People.Sex.MAN)
                || fromAge <= people.getAge()
                && people.getAge() <= femaleToAge
                && people.getSex().equals(People.Sex.WOMEN);

        return peopleList.stream()
                .filter(selectWorkablePeople)
                .collect(Collectors.toList());
    }

    /**
     * Given a List of `People` instances (having `name`, `age`, `sex` and `List of cats` fields,
     * and each `Cat` having a `name` and `age`),
     * return the names of all cats whose owners are women from `femaleAge` years old inclusively.
     */
    public List<String> getCatsNames(List<People> peopleList, int femaleAge) {
        Predicate<People> selectCatsOwners = people -> femaleAge <= people.getAge()
                && people.getSex().equals(People.Sex.WOMEN)
                && !people.getCats().isEmpty();

        return peopleList.stream()
                .filter(selectCatsOwners)
                .flatMap(owners -> owners.getCats().stream())
                .map(Cat::getName)
                .collect(Collectors.toList());
    }

    /**
     * Your help with a election is needed. Given list of candidates, where each element
     * has Candidate.class type.
     * Check which candidates are eligible to apply for president position and return their
     * names sorted alphabetically.
     * The requirements are: person should be older than 35 y, should be allowed to vote,
     * have nationality - 'Ukrainian'
     * and live in urk for 10 years. For the last requirement use field periodsInUkr,
     * which has following view: "2002-2015"
     * We want to reuse our validation in future, so let's write our own impl of Predicate
     * parametrized with Candidate in CandidateValidator.
     */
    public static List<String> validateCandidates(List<Candidate> candidates) {
        return candidates.stream()
                .filter(new CandidateValidator())
                .map(Candidate::getName)
                .sorted()
                .collect(Collectors.toList());
    }
}
