package com.loserico.java8.stream;

import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Winner {
	private int year;
	private String nationality;
	private String name;
	private String team;
	private int lengthKm;
	private Duration winningTime;
	private int stageWins;
	private int daysInYellow;

	public Winner(int year, String nationality, String name, String team, int lengthKm, Duration winningTime,
			int daysInYellow) {
		this.year = year;
		this.nationality = nationality;
		this.name = name;
		this.team = team;
		this.lengthKm = lengthKm;
		this.winningTime = winningTime;
		this.daysInYellow = daysInYellow;
	}

	public static final List<Winner> tdfWinners = Arrays.asList(
			new Winner(2006, "Spain", "Óscar Pereiro", "Caisse d'Epargne–Illes Balears", 3657, Duration.parse("PT89H40M27S"), 8),
			new Winner(2007, "Spain", "Alberto Contador", "Discovery Channel", 3570, Duration.parse("PT91H00M26S"), 4),
			new Winner(2008, "Spain", "Carlos Sastre", "Team CSC", 3559, Duration.parse("PT87H52M52S"), 5),
			new Winner(2009, "Spain", "Alberto Contador", "Astana", 3459, Duration.parse("PT85H48M35S"), 7),
			new Winner(2010, "Luxembourg", "Andy Schleck", "Team Saxo Bank", 3642, Duration.parse("PT91H59M27S"), 12),
			new Winner(2011, "Australia", "Cadel Evans", "BMC Racing Team", 3430, Duration.parse("PT86H12M22S"), 2),
			new Winner(2012, "Great Britain", "Bradley Wiggins", "Team Sky", 3496, Duration.parse("PT87H34M47S"), 14),
			new Winner(2013, "Great Britain", "Chris Froome", "Team Sky", 3404, Duration.parse("PT83H56M20S"), 14),
			new Winner(2014, "Italy", "Vincenzo Nibali", "Astana", 3661, Duration.parse("PT89H59M06S"), 19),
			new Winner(2015, "Great Britain", "Chris Froome", "Team Sky", 3360, Duration.parse("PT84H46M14S"), 16),
			new Winner(2016, "Great Britain", "Chris Froome", "Team Sky", 3529, Duration.parse("PT89H04M48S"), 14));

	public static void main(String args[]) {
		// Filter and Map -
		List<String> winnersOfToursLessThan3500km = tdfWinners
				.stream()
				.filter(d -> d.getLengthKm() < 3500) // Separate out Tours less than 3500km
				.map(Winner::getName) // Get names of winners
				.collect(toList()); // Return a list
		// Winners of Tours Less than 3500km - [Alberto Contador, Cadel Evans, Bradley Wiggins, Chris Froome, Chris Froome]        
		System.out.println("Winners of Tours Less than 3500km - " + winnersOfToursLessThan3500km);
		List<String> winnersOfToursGreaterThan3500km = tdfWinners
				.stream()
				.filter(d -> d.getLengthKm() >= 3500)
				.map(Winner::getName)
				.collect(toList());
		// Winners of Tours Greater than 3500km - [Óscar Pereiro, Alberto Contador, Carlos Sastre, Andy Schleck, Vincenzo Nibali, Chris Froome]
		System.out.println("Winners of Tours Greater than 3500km - " + winnersOfToursGreaterThan3500km);
		// limit - 
		List<Winner> winnerObjectsOfToursLessThan3500kmLimit2 = tdfWinners
				.stream()
				.filter(d -> d.getLengthKm() < 3500)
				.limit(2)
				.collect(toList());
		// winnerObjectsOfToursLessThan3500kmLimit2 [Alberto Contador, Cadel Evans]
		System.out.println("winnerObjectsOfToursLessThan3500kmLimit2 " + winnerObjectsOfToursLessThan3500kmLimit2);
		List<String> firstTwoWinnersOfToursLessThan3500km = tdfWinners
				.stream()
				.filter(d -> d.getLengthKm() < 3500)
				.map(Winner::getName)
				.limit(2)
				.collect(toList());
		// firstTwoWinnersOfToursLessThan3500km - [Alberto Contador, Cadel Evans]
		System.out.println("firstTwoWinnersOfToursLessThan3500km - " + firstTwoWinnersOfToursLessThan3500km);
		// filter by distinct
		List<String> distinctTDFWinners = tdfWinners
				.stream()
				.map(Winner::getName)
				.distinct()
				.collect(toList());
		System.out.println("distinctTDFWinners - " + distinctTDFWinners);
		long numberOfDistinceWinners = tdfWinners
				.stream()
				.map(Winner::getName)
				.distinct()
				.count();
		// numberOfDistinceWinners - 8
		System.out.println("numberOfDistinceWinners - " + numberOfDistinceWinners);
		// skip records
		List<Winner> skipEveryOtherTDFWinner = tdfWinners
				.stream()
				.skip(2)
				.collect(toList());
		// skipEveryOtherTDFWinner - [Carlos Sastre, Alberto Contador, Andy Schleck, Cadel Evans, Bradley Wiggins, Chris Froome, Vincenzo Nibali, Chris Froome, Chris Froome]
		System.out.println("skipEveryOtherTDFWinner - " + skipEveryOtherTDFWinner);
		List<String> mapWinnerYearNamesToList = tdfWinners
				.stream()
				.map(w -> w.getYear() + " - " + w.getName())
				.collect(toList());
		// mapWinnerYearNamesToList [2006 - Óscar Pereiro, 2007 - Alberto Contador, 2008 - Carlos Sastre, 2009 - Alberto Contador, 2010 - Andy Schleck, 2011 - Cadel Evans, 2012 - Bradley Wiggins, 2013 - Chris Froome, 2014 - Vincenzo Nibali, 2015 - Chris Froome, 2016 - Chris Froome]
		System.out.println("mapWinnerYearNamesToList " + mapWinnerYearNamesToList);
		List<Integer> mapWinnerNameLengthToList = tdfWinners
				.stream()
				.map(Winner::getName)
				.map(String::length)
				.collect(toList());
		// mapWinnerNameLengthToList [13, 16, 13, 16, 12, 11, 15, 12, 15, 12, 12]
		System.out.println("mapWinnerNameLengthToList " + mapWinnerNameLengthToList);
		// matching - allMatch, noneMatch
		Optional<Winner> winner2012 = tdfWinners.stream().filter(w -> w.getName().contains("Wiggins")).findAny();
		// winner2012 - Bradley Wiggins
		System.out.println("winner2012 - " + winner2012.get());
		Optional<Integer> winnerYear2014 = tdfWinners.stream().map(Winner::getYear).filter(x -> x == 2014).findFirst();
		// winnerYear2014 - 2014
		System.out.println("winnerYear2014 - " + winnerYear2014.get());
		// reducing - 0 --> initial value
		int totalDistance = tdfWinners.stream().map(Winner::getLengthKm).reduce(0, Integer::sum);
		// totalDistance - 38767
		System.out.println("totalDistance - " + totalDistance);
		Optional<Integer> shortestYear = tdfWinners.stream().map(Winner::getLengthKm).reduce(Integer::min);
		// shortestYear - 3360
		System.out.println("shortestYear - " + shortestYear.get());
		Optional<Integer> longestYear = tdfWinners.stream().map(Winner::getLengthKm).reduce(Integer::max);
		// longestYear - 3661
		System.out.println("longestYear - " + longestYear.get());
		Optional<Winner> fastestWinner = tdfWinners.stream().min(Comparator.comparingDouble(Winner::getAveSpeed));
		System.out.println("fastestTDF - " + fastestWinner.get());
		// shorthand
		OptionalDouble fastestTDF = tdfWinners.stream().mapToDouble(Winner::getAveSpeed).min();
		// fastestTDF - 39.0
		System.out.println("fastestTDF - " + fastestTDF.getAsDouble());
		// groupingby - make a map whose keys are names
		Map<String, List<Winner>> namesVsWinner = tdfWinners.stream().collect(groupingBy(Winner::getName));
		// namesVsWinner - {Bradley Wiggins=[Bradley Wiggins], Carlos Sastre=[Carlos Sastre], Cadel Evans=[Cadel Evans], Óscar Pereiro=[Óscar Pereiro], Chris Froome=[Chris Froome, Chris Froome, Chris Froome], Andy Schleck=[Andy Schleck], Alberto Contador=[Alberto Contador, Alberto Contador], Vincenzo Nibali=[Vincenzo Nibali]}
		System.out.println("namesVsWinner - " + namesVsWinner);
		// join strings
		String allTDFWinnersTeamsCSV = tdfWinners.stream().map(Winner::getTeam).collect(joining(", "));
		// allTDFWinnersTeams Caisse d'Epargne–Illes Balears, Discovery Channel, Team CSC, Astana, Team Saxo Bank, BMC Racing Team, Team Sky, Team Sky, Astana, Team Sky, Team Sky
		System.out.println("allTDFWinnersTeams " + allTDFWinnersTeamsCSV);
		// grouping
		Map<String, List<Winner>> winnersByNationality = tdfWinners.stream()
				.collect(groupingBy(Winner::getNationality));
		// winnersByNationality - {Great Britain=[Bradley Wiggins, Chris Froome, Chris Froome, Chris Froome], Luxembourg=[Andy Schleck], Italy=[Vincenzo Nibali], Australia=[Cadel Evans], Spain=[Óscar Pereiro, Alberto Contador, Carlos Sastre, Alberto Contador]}
		System.out.println("winnersByNationality - " + winnersByNationality);
		Map<String, Long> winsByNationalityCounting = tdfWinners.stream()
				.collect(groupingBy(Winner::getNationality, counting()));
		// winsByNationalityCounting - {Great Britain=4, Luxembourg=1, Italy=1, Australia=1, Spain=4}
		System.out.println("winsByNationalityCounting - " + winsByNationalityCounting);
	}

	public double getAveSpeed() {
		return (getLengthKm() / (getWinningTime().getSeconds() / 3600));
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public int getLengthKm() {
		return lengthKm;
	}

	public void setLengthKm(int lengthKm) {
		this.lengthKm = lengthKm;
	}

	public Duration getWinningTime() {
		return winningTime;
	}

	public void setWinningTime(Duration winningTime) {
		this.winningTime = winningTime;
	}

	public int getStageWins() {
		return stageWins;
	}

	public void setStageWins(int stageWins) {
		this.stageWins = stageWins;
	}

	public int getDaysInYellow() {
		return daysInYellow;
	}

	public void setDaysInYellow(int daysInYellow) {
		this.daysInYellow = daysInYellow;
	}

	@Override
	public String toString() {
		return name;
	}
}