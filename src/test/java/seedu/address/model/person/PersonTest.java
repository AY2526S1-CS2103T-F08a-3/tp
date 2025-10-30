package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PROPERTY_TYPE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void hasSameIdentifier() {
        // same object -> returns true
        assertTrue(ALICE.hasSameIdentifier(ALICE));

        // null -> returns false
        assertFalse(ALICE.hasSameIdentifier(null));

        // same phone, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).withEmail(VALID_EMAIL_BOB)
                .withRole(VALID_ROLE_BOB).withAddress(VALID_ADDRESS_BOB, VALID_PROPERTY_TYPE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.hasSameIdentifier(editedAlice));

        // different phone, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.hasSameIdentifier(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different role -> returns false
        editedAlice = new PersonBuilder(ALICE).withRole(VALID_ROLE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB, VALID_PROPERTY_TYPE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void isSeller() {
        Person seller = new PersonBuilder().withRole(Role.SELLER).build();
        Person buyer = new PersonBuilder().withRole(Role.BUYER).build();
        // is a seller -> returns true
        assertTrue(seller.isSeller());

        // is not a seller -> returns false
        assertFalse(buyer.isSeller());
    }

    @Test
    public void isBuyer() {
        Person seller = new PersonBuilder().withRole(Role.SELLER).build();
        Person buyer = new PersonBuilder().withRole(Role.BUYER).build();

        // is a buyer -> returns true
        assertTrue(buyer.isBuyer());

        // is not a buyer -> returns false
        assertFalse(seller.isBuyer());
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", role=" + ALICE.getRole() + ", address=" + ALICE.getAddress()
                + ", tags=" + ALICE.getTags() + "}";
        assertEquals(expected, ALICE.toString());
    }

    @Test
    public void containsKeyword() {
        // Use existing person from TypicalPersons
        Person person = ALICE;

        // Name matching
        assertTrue(person.containsKeyword("Alice"));
        assertTrue(person.containsKeyword("Pauline"));

        // Role matching
        assertTrue(person.containsKeyword("buyer"));

        // Phone matching
        assertTrue(person.containsKeyword("94351253"));

        // Email matching
        assertTrue(person.containsKeyword("alice@example.com"));

        // Address matching
        assertTrue(person.containsKeyword("Jurong"));
        assertTrue(person.containsKeyword("West"));

        // Property type matching
        assertTrue(person.containsKeyword("HDB_2"));

        // Tag matching
        assertTrue(person.containsKeyword("friends"));

        // Case insensitive matching
        assertTrue(person.containsKeyword("ALICE"));
        assertTrue(person.containsKeyword("jurong"));

        // Non-matching keywords
        assertFalse(person.containsKeyword("Charlie"));
        assertFalse(person.containsKeyword("seller"));
        assertFalse(person.containsKeyword("nonexistent"));

        // Partial word matching should not work (StringUtil word boundary requirement)
        assertFalse(person.containsKeyword("Ali")); // partial match of "Alice"
        assertFalse(person.containsKeyword("9435")); // partial match of "94351253"
    }
}
