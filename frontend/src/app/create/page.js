"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

export default function CreateRecipe() {
  const router = useRouter();

  // Αρχική κατάσταση της συνταγής
  const [recipe, setRecipe] = useState({
    name: "",
    category: "",
    difficulty: "Easy",
    totalTime: 0,
    ingredients: [],
    steps: [],
  });

  // State για προσωρινή προσθήκη ενός υλικού
  const [tempIngredient, setTempIngredient] = useState({
    name: "",
    quantity: "",
    unit: "",
  });

  // State για προσωρινή προσθήκη ενός βήματος
  const [tempStep, setTempStep] = useState({
    title: "",
    description: "",
    duration: "",
    photoUrl: "",
    ingredients: "",
    selectedIngredients: [],
  });

  // 1. Αποστολή στο Backend
  const handleSubmit = async (e) => {
    e.preventDefault(); // Επαλήθευση ότι υπάρχουν βήματα και υλικά

    if (recipe.ingredients.length === 0 || recipe.steps.length === 0) {
      alert("Πρέπει να προσθέσεις τουλάχιστον ένα υλικό και ένα βήμα!");
      return;
    }

    // Υπολογισμός του αθροίσματος των βημάτων
    const stepsSum = recipe.steps.reduce(
      (sum, step) => sum + parseInt(step.duration || 0),
      0
    );

    // Έλεγχος Ακεραιότητας: Ο συνολικός χρόνος πρέπει να ισούται με το άθροισμα των βημάτων
    // Σημείωση: Χρησιμοποιούμε != αντί για !== για να αποφύγουμε προβλήματα string/number
    if (parseInt(recipe.totalTime) != stepsSum) {
      alert(
        `Σφάλμα Χρόνου! \n\nΔηλώσατε Συνολικό Χρόνο: ${recipe.totalTime}'\nΆθροισμα Βημάτων: ${stepsSum}'\n\nΠαρακαλώ διορθώστε τους χρόνους ώστε να ταιριάζουν.`
      );
      return; // Σταματάμε εδώ. Δεν στέλνουμε τίποτα στον server.
    }

    try {
      const res = await fetch("http://localhost:8081/api/recipes", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(recipe),
      });

      if (res.ok) {
        alert("Η συνταγή αποθηκεύτηκε!");
        router.push("/"); // Επιστροφή στην αρχική
      } else {
        alert("Κάτι πήγε στραβά.");
      }
    } catch (err) {
      console.error(err);
      alert("Σφάλμα σύνδεσης με τον server.");
    }
  };

  // --- Βοηθητικές συναρτήσεις ---

  const addIngredient = () => {
    if (!tempIngredient.name) return;
    setRecipe({
      ...recipe,
      ingredients: [...recipe.ingredients, tempIngredient],
    });
    setTempIngredient({ name: "", quantity: "", unit: "" }); // Καθαρισμός
  };

  // Συνάρτηση διαγραφής υλικού από την κεντρική λίστα
  const removeIngredient = (indexToRemove) => {
    setRecipe({
      ...recipe,
      ingredients: recipe.ingredients.filter((_, idx) => idx !== indexToRemove),
    });
  };

  // Συνάρτηση για να τσεκάρεις/ξε-τσεκάρεις υλικά στο βήμα
  const toggleStepIngredient = (ingredientName) => {
    const currentList = tempStep.selectedIngredients;

    if (currentList.includes(ingredientName)) {
      // Αν υπάρχει, το αφαιρούμε
      setTempStep({
        ...tempStep,
        selectedIngredients: currentList.filter(
          (name) => name !== ingredientName
        ),
      });
    } else {
      // Αν δεν υπάρχει, το προσθέτουμε
      setTempStep({
        ...tempStep,
        selectedIngredients: [...currentList, ingredientName],
      });
    }
  };

  const addStep = () => {
    if (!tempStep.title) return;

    const stepPhotoList = tempStep.photoUrl ? [tempStep.photoUrl] : [];

    const newStep = {
      ...tempStep,
      stepOrder: recipe.steps.length + 1,
      stepIngredients: tempStep.selectedIngredients, // Το περνάμε απευθείας!
      stepPhotos: stepPhotoList,
    };

    setRecipe({ ...recipe, steps: [...recipe.steps, newStep] });
    setTempStep({
      title: "",
      description: "",
      duration: "",
      photoUrl: "",
      ingredients: "",
      selectedIngredients: [],
    });
  };

  // 1. Διαγραφή ενός βήματος από τη λίστα
  const removeStep = (indexToRemove) => {
    // Κρατάμε όλα τα βήματα ΕΚΤΟΣ από αυτό που θέλουμε να σβήσουμε
    const updatedSteps = recipe.steps.filter((_, idx) => idx !== indexToRemove);

    // Ξανα-υπολογίζουμε τη σειρά (1, 2, 3...) για να μην υπάρχουν κενά
    const reorderedSteps = updatedSteps.map((step, idx) => ({
      ...step,
      stepOrder: idx + 1,
    }));

    setRecipe({ ...recipe, steps: reorderedSteps });
  };

  // 2. Επεξεργασία (Φόρτωση στη φόρμα και διαγραφή από τη λίστα)
  const editStep = (indexToEdit) => {
    const stepToEdit = recipe.steps[indexToEdit];

    setTempStep({
      title: stepToEdit.title,
      description: stepToEdit.description,
      duration: stepToEdit.duration,
      photoUrl:
        stepToEdit.stepPhotos && stepToEdit.stepPhotos.length > 0
          ? stepToEdit.stepPhotos[0]
          : "",
      // Φόρτωση των επιλεγμένων υλικών πίσω στα checkboxes
      selectedIngredients: stepToEdit.stepIngredients || [],
    });

    removeStep(indexToEdit);
  };

  return (
    <div className="min-h-screen p-8 max-w-4xl mx-auto bg-white shadow-xl mt-10 rounded-xl">
      <h1 className="text-3xl font-bold mb-6 text-gray-800">🍳 Νέα Συνταγή</h1>

      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Βασικά Στοιχεία */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <input
            type="text"
            placeholder="Όνομα Συνταγής (π.χ. Καρμπονάρα)"
            className="border p-2 rounded w-full"
            value={recipe.name}
            onChange={(e) => setRecipe({ ...recipe, name: e.target.value })}
            required
          />
          <input
            type="text"
            placeholder="Κατηγορία (π.χ. Ζυμαρικά)"
            className="border p-2 rounded w-full"
            value={recipe.category}
            onChange={(e) => setRecipe({ ...recipe, category: e.target.value })}
            required
          />
          <select
            className="border p-2 rounded w-full"
            value={recipe.difficulty}
            onChange={(e) =>
              setRecipe({ ...recipe, difficulty: e.target.value })
            }
          >
            <option value="Easy">Εύκολη</option>
            <option value="Medium">Μέτρια</option>
            <option value="Hard">Δύσκολη</option>
          </select>
          <input
            type="number"
            placeholder="Συνολικός Χρόνος (λεπτά)"
            className="border p-2 rounded w-full"
            value={recipe.totalTime}
            onChange={(e) =>
              setRecipe({ ...recipe, totalTime: parseInt(e.target.value) || 0 })
            }
            required
          />
        </div>

        <hr />

        {/* Ενότητα Υλικών */}
        <div>
          <h3 className="text-xl font-semibold mb-2">🛒 Υλικά</h3>
          <div className="flex gap-2 mb-2">
            <input
              type="text"
              placeholder="Υλικό"
              className="border p-2 flex-1"
              value={tempIngredient.name}
              onChange={(e) =>
                setTempIngredient({ ...tempIngredient, name: e.target.value })
              }
            />
            <input
              type="number"
              placeholder="Ποσότητα"
              className="border p-2 w-24"
              value={tempIngredient.quantity}
              onChange={(e) =>
                setTempIngredient({
                  ...tempIngredient,
                  quantity: e.target.value,
                })
              }
            />
            <input
              type="text"
              placeholder="Μονάδα (π.χ. gr)"
              className="border p-2 w-24"
              value={tempIngredient.unit}
              onChange={(e) =>
                setTempIngredient({ ...tempIngredient, unit: e.target.value })
              }
            />
            <button
              type="button"
              onClick={addIngredient}
              className="bg-blue-500 text-white px-4 rounded"
            >
              +
            </button>
          </div>
          {/* Λίστα προστεθέντων υλικών */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-3 mt-3">
            {recipe.ingredients.map((ing, idx) => (
              <div
                key={idx}
                className="flex justify-between items-center bg-blue-50 p-3 rounded-lg border border-blue-100 shadow-sm"
              >
                <div className="flex items-center gap-2">
                  <span className="bg-blue-200 text-blue-800 text-xs font-bold px-2 py-1 rounded-full">
                    {idx + 1}
                  </span>
                  <span className="font-semibold text-gray-700">
                    {ing.name}
                  </span>
                  <span className="text-gray-500 text-sm">
                    ({ing.quantity} {ing.unit})
                  </span>
                </div>
                <button
                  type="button"
                  onClick={() => removeIngredient(idx)}
                  className="text-red-400 hover:text-red-600 hover:bg-red-50 p-1 rounded transition"
                  title="Αφαίρεση υλικού"
                >
                  ✕
                </button>
              </div>
            ))}

            {recipe.ingredients.length === 0 && (
              <p className="text-gray-400 text-sm italic col-span-2 text-center">
                Δεν έχουν προστεθεί υλικά ακόμα.
              </p>
            )}
          </div>
        </div>

        <hr />

        {/* Ενότητα Βημάτων */}
        <div>
          <h3 className="text-xl font-semibold mb-2">👣 Βήματα Εκτέλεσης</h3>
          <div className="space-y-3 border p-4 rounded bg-gray-50">
            {/* Title & Duration */}
            <div className="flex gap-2">
              <input
                type="text"
                placeholder="Τίτλος Βήματος (π.χ. Βράσιμο)"
                className="border p-2 w-2/3 rounded"
                value={tempStep.title}
                onChange={(e) =>
                  setTempStep({ ...tempStep, title: e.target.value })
                }
              />
              <input
                type="number"
                placeholder="Λεπτά"
                className="border p-2 w-1/3 rounded"
                value={tempStep.duration}
                onChange={(e) =>
                  setTempStep({
                    ...tempStep,
                    duration: parseInt(e.target.value) || 0,
                  })
                }
              />
            </div>
            {/* Description */}
            <textarea
              placeholder="Περιγραφή (Τι κάνουμε;)"
              className="border p-2 w-full rounded"
              value={tempStep.description}
              onChange={(e) =>
                setTempStep({ ...tempStep, description: e.target.value })
              }
            />
            {/* --- ΦΩΤΟΓΡΑΦΙΑ ΒΗΜΑΤΟΣ (Με προεπισκόπηση) --- */}
            <div>
              <input
                type="text"
                placeholder="URL Φωτογραφίας Βήματος (Link)"
                className="border p-2 w-full rounded focus:ring-2 focus:ring-purple-400 outline-none"
                value={tempStep.photoUrl}
                onChange={(e) =>
                  setTempStep({ ...tempStep, photoUrl: e.target.value })
                }
              />
              {/* Εμφάνιση εικόνας αν υπάρχει έγκυρο URL */}
              {tempStep.photoUrl && (
                <div className="mt-2 w-full h-32 bg-gray-100 rounded overflow-hidden border">
                  <img
                    src={tempStep.photoUrl}
                    alt="Προεπισκόπηση βήματος"
                    className="w-full h-full object-cover"
                    onError={(e) => (e.target.style.display = "none")} // Κρύψιμο αν το link είναι χαλασμένο
                  />
                </div>
              )}
            </div>

            {/* --- ΕΠΙΛΟΓΗ ΥΛΙΚΩΝ ΒΗΜΑΤΟΣ (Styling) --- */}
            <div className="bg-white p-4 border rounded shadow-sm">
              <label className="block text-sm font-bold text-gray-700 mb-3">
                {recipe.ingredients.length > 0
                  ? "📦 Επιλέξτε ποια υλικά χρησιμοποιούνται σε αυτό το βήμα:"
                  : "⚠️ Προσθέστε πρώτα υλικά στην αρχή της σελίδας."}
              </label>

              <div className="flex flex-wrap gap-2">
                {recipe.ingredients.map((ing, idx) => {
                  // Ελέγχουμε αν το υλικό είναι επιλεγμένο
                  const isSelected = (
                    tempStep.selectedIngredients || []
                  ).includes(ing.name);
                  return (
                    <label
                      key={idx}
                      className={`
                                    flex items-center space-x-2 px-3 py-2 rounded-lg cursor-pointer border transition-all
                                    ${
                                      isSelected
                                        ? "bg-green-100 border-green-500 text-green-800 shadow-md transform scale-105"
                                        : "bg-gray-50 border-gray-200 text-gray-600 hover:bg-gray-100"
                                    }
                                `}
                    >
                      <input
                        type="checkbox"
                        checked={isSelected}
                        onChange={() => toggleStepIngredient(ing.name)}
                        className="accent-green-600 w-4 h-4"
                      />
                      <span className="text-sm font-medium">{ing.name}</span>
                    </label>
                  );
                })}
              </div>
            </div>
            <button
              type="button"
              onClick={addStep}
              className="bg-purple-500 text-white px-4 py-2 rounded w-full font-bold"
            >
              Προσθήκη Βήματος
            </button>
          </div>
          {/* Λίστα προστεθέντων βημάτων */}
          <div className="mt-4 space-y-3">
            {recipe.steps.map((step, idx) => (
              <div
                key={idx}
                className="bg-white border rounded-lg overflow-hidden shadow-sm mt-3"
              >
                {/* Header Βήματος (Τίτλος & Κουμπιά) */}
                <div className="bg-gray-50 p-3 flex justify-between items-center border-b">
                  <span className="font-bold text-gray-800">
                    {step.stepOrder}. {step.title}
                    <span className="ml-2 text-xs bg-blue-100 text-blue-800 px-2 py-1 rounded-full">
                      ⏱️ {step.duration}'
                    </span>
                  </span>
                  <div className="flex gap-2">
                    <button
                      type="button"
                      onClick={() => editStep(idx)}
                      className="text-blue-500 hover:text-blue-700 bg-white border p-1 rounded"
                      title="Επεξεργασία"
                    >
                      ✏️
                    </button>
                    <button
                      type="button"
                      onClick={() => removeStep(idx)}
                      className="text-red-500 hover:text-red-700 bg-white border p-1 rounded"
                      title="Διαγραφή"
                    >
                      🗑️
                    </button>
                  </div>
                </div>

                <div className="p-4">
                  {/* Περιγραφή */}
                  <p className="text-gray-600 mb-3">{step.description}</p>

                  {/* Εμφάνιση Υλικών Βήματος (Αν υπάρχουν) */}
                  {step.stepIngredients && step.stepIngredients.length > 0 && (
                    <div className="mb-3">
                      <span className="text-xs font-bold text-gray-500 uppercase tracking-wide">
                        Υλικα Βηματος:
                      </span>
                      <div className="flex flex-wrap gap-2 mt-1">
                        {step.stepIngredients.map((ing, i) => (
                          <span
                            key={i}
                            className="text-xs bg-green-50 text-green-700 border border-green-200 px-2 py-1 rounded-md font-medium"
                          >
                            🛒 {ing}
                          </span>
                        ))}
                      </div>
                    </div>
                  )}

                  {/* Εμφάνιση Φωτογραφίας (Αν υπάρχει) */}
                  {step.stepPhotos && step.stepPhotos.length > 0 && (
                    <div className="mt-2">
                      <span className="text-xs font-bold text-gray-500 uppercase tracking-wide">
                        Φωτογραφια:
                      </span>
                      <img
                        src={step.stepPhotos[0]}
                        alt={`Step ${step.stepOrder}`}
                        className="mt-1 h-32 w-full object-cover rounded-md border"
                      />
                    </div>
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Κουμπί Αποθήκευσης */}
        <button
          type="submit"
          className="w-full bg-green-600 text-white font-bold py-3 rounded-lg hover:bg-green-700 text-lg"
        >
          Αποθήκευση Συνταγής ✅
        </button>
      </form>
    </div>
  );
}
