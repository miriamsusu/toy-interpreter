package interpreter.controller;

import interpreter.model.type.IntType;
import interpreter.model.type.RefType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.RefValue;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GarbageCollectorTest {
    // the state of Ex9 after new(v, 30): v -> (3, int), a -> (2, Ref(int)); 4 is garbage
    private final List<IValue> symTable = List.of(
            new RefValue(3, new IntType()), new RefValue(2, new RefType(new IntType())));
    private final Map<Integer, IValue> heap = Map.of(
            1, new IntValue(20), 2, new RefValue(1, new IntType()),
            3, new IntValue(30), 4, new IntValue(7));

    @Test
    void addressesFromSymTableAreOnlyTheReferences() {
        List<IValue> values = List.of(new IntValue(5), new RefValue(3, new IntType()), new IntValue(3));
        assertEquals(Set.of(3), GarbageCollector.getAddrFromSymTable(values));
    }

    @Test
    void unsafeCollectorRemovesALocationReachableOnlyThroughTheHeap() {
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(symTable);
        Map<Integer, IValue> kept = GarbageCollector.unsafeGarbageCollector(roots, heap);
        assertEquals(Set.of(2, 3), kept.keySet());   // 1 is lost: this is why it is unsafe
    }

    @Test
    void keepsTheLocationsReachableThroughTheHeap() {
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(symTable);
        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(roots, heap);
        assertEquals(Set.of(1, 2, 3), kept.keySet());   // 4 is garbage, 1 is not
    }

    @Test
    void keptLocationsKeepTheirValues() {
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(symTable);
        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(roots, heap);
        assertEquals(new IntValue(20), kept.get(1));
        assertEquals(new RefValue(1, new IntType()), kept.get(2));
    }

    @Test
    void unreachableLocationsAreRemoved() {
        Map<Integer, IValue> kept = GarbageCollector.safeGarbageCollector(Set.of(3), heap);
        assertEquals(Set.of(3), kept.keySet());
    }

    @Test
    void noReferencesRemovesEverything() {
        assertTrue(GarbageCollector.safeGarbageCollector(Set.of(), heap).isEmpty());
    }

    @Test
    void reachableAddressesFollowsAChain() {
        // 5 -> (6, ...), 6 -> (7, ...), 7 -> 1: only 5 is a root
        Map<Integer, IValue> chain = Map.of(
                5, new RefValue(6, new RefType(new IntType())),
                6, new RefValue(7, new IntType()),
                7, new IntValue(1),
                8, new IntValue(2));
        assertEquals(Set.of(5, 6, 7), GarbageCollector.reachableAddresses(Set.of(5), chain));
    }

    @Test
    void reachableAddressesStopsOnACycle() {
        Map<Integer, IValue> cycle = Map.of(
                1, new RefValue(2, new IntType()),
                2, new RefValue(1, new IntType()));
        assertEquals(Set.of(1, 2), GarbageCollector.reachableAddresses(Set.of(1), cycle));
    }

    @Test
    void aNullReferenceIsIgnored() {
        // a Ref variable that was only declared has address 0, which is never in the heap
        Set<Integer> roots = GarbageCollector.getAddrFromSymTable(List.of(new RefValue(0, new IntType())));
        assertTrue(GarbageCollector.safeGarbageCollector(roots, heap).isEmpty());
    }
}
