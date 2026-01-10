"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";

export default function CreateRecipe() {
  const router = useRouter();
  const [loadingLists, setLoadingLists] = useState(true);

  // --- Λίστες για Dropdowns ---
  const [categoriesList, setCategoriesList] = useState([]);
  const [unitsList, setUnitsList] = useState([]);
  const [difficultiesList, setDifficultiesList] = useState([]);

  // --- State Φόρμας ---
  const [recipe, setRecipe] = useState({
    name: "",
    category: "", // Θα γεμίσει από το API
    difficulty: "", // Θα γεμίσει από το API
    totalTime: 0,
    ingredients: [],
    steps: [],
    photoUrls: [],
  });

  // State για προσωρινό υλικό
  const [tempIngredient, setTempIngredient] = useState({
    name: "",
    quantity: "",
    unit: "", // Θα γεμίσει από το API
  });

  // State για προσωρινό βήμα
  const [tempStep, setTempStep] = useState({
    title: "",
    description: "",
    duration: "",
    photoUrl: "",
    selectedIngredients: [], // Ονόματα υλικών που επιλέχθηκαν για το βήμα
  });

  // 1. Φόρτωση Λιστών από το ReferenceController
  useEffect(() => {
    const fetchData = async () => {
      try {
        // Fetch Categories
        const catRes = await fetch(
          "http://localhost:8081/api/references/categories"
        );
        const catData = await catRes.json();
        setCategoriesList(catData);

        // Fetch Units
        const unitRes = await fetch(
          "http://localhost:8081/api/references/units"
        );
        const unitData = await unitRes.json();
        setUnitsList(unitData);

        // Fetch Difficulties
        const diffRes = await fetch(
          "http://localhost:8081/api/references/difficulties"
        );
        const diffData = await diffRes.json();
        setDifficultiesList(diffData);

        // --- Ρύθμιση Default Τιμών ---
        // Για να μην είναι κενά τα selects αν ο χρήστης δεν τα πειράξει
        setRecipe((prev) => ({
          ...prev,
          category: catData[0] || "Ζυμαρικά",
          difficulty: diffData[0] || "Εύκολη",
        }));
        setTempIngredient((prev) => ({
          ...prev,
          unit: unitData[0] || "gr",
        }));

        setLoadingLists(false);
      } catch (err) {
        console.error("Σφάλμα στη φόρτωση λιστών:", err);
        // Fallback αν δεν τρέχει το backend ακόμα
        setCategoriesList(["Ζυμαρικά", "Κρεατικά", "Λαδερά"]);
        setUnitsList(["gr", "kg", "ml", "τεμάχια"]);
        setDifficultiesList(["Εύκολη", "Μέτρια", "Δύσκολη"]);
        setLoadingLists(false);
      }
    };

    fetchData();
  }, []);

  // 2. Υποβολή Φόρμας
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (recipe.ingredients.length === 0 || recipe.steps.length === 0) {
      alert("Πρέπει να προσθέσεις τουλάχιστον ένα υλικό και ένα βήμα!");
      return;
    }

    // Δημιουργία Payload για το Backend
    const payload = {
      name: recipe.name,
      category: recipe.category,
      difficulty: recipe.difficulty,
      totalTime: parseInt(recipe.totalTime),
      photoUrls: [], // Θα μπορούσε να μπει φωτογραφία συνταγής εδώ

      // Μετατροπή Υλικών
      ingredients: recipe.ingredients.map((ing) => ({
        name: ing.name,
        quantity: parseFloat(ing.quantity),
        unit: ing.unit,
      })),

      // Μετατροπή Βημάτων
      steps: recipe.steps.map((step) => {
        // Βρίσκουμε τα πλήρη αντικείμενα των επιλεγμένων υλικών
        const stepIngredientsObjects = recipe.ingredients
          .filter((ing) => step.selectedIngredients.includes(ing.name))
          .map((ing) => ({
            name: ing.name,
            quantity: parseFloat(ing.quantity),
            unit: ing.unit,
          }));

        return {
          title: step.title,
          description: step.description,
          duration: parseInt(step.duration),
          photoUrls: step.photoUrl ? [step.photoUrl] : [],
          ingredients: stepIngredientsObjects,
        };
      }),
    };

    try {
      const res = await fetch("http://localhost:8081/api/recipes", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (res.ok) {
        alert("Η συνταγή αποθηκεύτηκε επιτυχώς!");
        router.push("/"); // Επιστροφή στην αρχική
      } else {
        alert("Κάτι πήγε στραβά κατά την αποθήκευση.");
      }
    } catch (err) {
      console.error(err);
      alert("Αδυναμία σύνδεσης με τον Server.");
    }
  };

  // --- Helper Functions ---

  const addIngredient = () => {
    if (!tempIngredient.name || !tempIngredient.quantity) {
      alert("Συμπλήρωσε όνομα και ποσότητα!");
      return;
    }
    setRecipe({
      ...recipe,
      ingredients: [...recipe.ingredients, tempIngredient],
    });
    // Καθαρίζουμε τα πεδία αλλά κρατάμε τη μονάδα ίδια για ταχύτητα
    setTempIngredient((prev) => ({ ...prev, name: "", quantity: "" }));
  };

  const removeIngredient = (idxToRemove) => {
    const ingredientToRemove = recipe.ingredients[idxToRemove];

    // Αφαίρεση από τη λίστα υλικών
    setRecipe((prev) => ({
      ...prev,
      ingredients: prev.ingredients.filter((_, idx) => idx !== idxToRemove),
      // Αφαίρεση και από τα βήματα αν είχε επιλεγεί εκεί (για να μην χαλάσει η λογική)
      steps: prev.steps.map((step) => ({
        ...step,
        selectedIngredients: step.selectedIngredients.filter(
          (name) => name !== ingredientToRemove.name
        ),
      })),
    }));
  };

  const toggleStepIngredient = (ingName) => {
    const current = tempStep.selectedIngredients;
    if (current.includes(ingName)) {
      setTempStep({
        ...tempStep,
        selectedIngredients: current.filter((n) => n !== ingName),
      });
    } else {
      setTempStep({ ...tempStep, selectedIngredients: [...current, ingName] });
    }
  };

  const addStep = () => {
    if (!tempStep.title || !tempStep.duration) {
      alert("Συμπλήρωσε τίτλο και διάρκεια βήματος!");
      return;
    }
    const newStep = {
      ...tempStep,
      stepOrder: recipe.steps.length + 1,
    };
    setRecipe({ ...recipe, steps: [...recipe.steps, newStep] });
    setTempStep({
      title: "",
      description: "",
      duration: "",
      photoUrl: "",
      selectedIngredients: [],
    });
  };

  const removeStep = (idxToRemove) => {
    const newSteps = recipe.steps
      .filter((_, idx) => idx !== idxToRemove)
      .map((step, idx) => ({ ...step, stepOrder: idx + 1 }));
    setRecipe({ ...recipe, steps: newSteps });
  };

  if (loadingLists)
    return <div className="text-center mt-10 text-xl">Φόρτωση επιλογών...</div>;

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-4xl mx-auto bg-white shadow-xl rounded-xl overflow-hidden">
        {/* Header */}
        <div className="bg-blue-600 p-6 flex justify-between items-center text-white">
          <h1 className="text-3xl font-bold">🍳 Δημιουργία Συνταγής</h1>
          <Link href="/">
            <button className="hover:underline">← Ακύρωση</button>
          </Link>
        </div>

        <form onSubmit={handleSubmit} className="p-8 space-y-8">
          {/* 1. Γενικά Στοιχεία */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block text-gray-700 font-bold mb-2">
                Όνομα Συνταγής
              </label>
              <input
                type="text"
                className="w-full border p-3 rounded focus:ring-2 focus:ring-blue-400 outline-none"
                placeholder="π.χ. Παστίτσιο"
                value={recipe.name}
                onChange={(e) => setRecipe({ ...recipe, name: e.target.value })}
                required
              />
            </div>
            <div>
              <label className="block text-gray-700 font-bold mb-2">
                Συνολικός Χρόνος (λεπτά)
              </label>
              <input
                type="number"
                className="w-full border p-3 rounded focus:ring-2 focus:ring-blue-400 outline-none"
                placeholder="π.χ. 60"
                value={recipe.totalTime}
                onChange={(e) =>
                  setRecipe({ ...recipe, totalTime: e.target.value })
                }
                required
              />
            </div>
            <div>
              <label className="block text-gray-700 font-bold mb-2">
                Κατηγορία
              </label>
              <select
                className="w-full border p-3 rounded focus:ring-2 focus:ring-blue-400 outline-none bg-white"
                value={recipe.category}
                onChange={(e) =>
                  setRecipe({ ...recipe, category: e.target.value })
                }
              >
                {categoriesList.map((c, i) => (
                  <option key={i} value={c}>
                    {c}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className="block text-gray-700 font-bold mb-2">
                Δυσκολία
              </label>
              <select
                className="w-full border p-3 rounded focus:ring-2 focus:ring-blue-400 outline-none bg-white"
                value={recipe.difficulty}
                onChange={(e) =>
                  setRecipe({ ...recipe, difficulty: e.target.value })
                }
              >
                {difficultiesList.map((d, i) => (
                  <option key={i} value={d}>
                    {d}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <hr className="border-gray-200" />

          {/* 2. Υλικά */}
          <div>
            <h2 className="text-xl font-bold text-gray-800 mb-4">
              🛒 Υλικά Συνταγής
            </h2>
            <div className="flex gap-2 mb-4 bg-gray-50 p-4 rounded border">
              <input
                type="text"
                placeholder="Όνομα (π.χ. Αλεύρι)"
                className="flex-1 border p-2 rounded"
                value={tempIngredient.name}
                onChange={(e) =>
                  setTempIngredient({ ...tempIngredient, name: e.target.value })
                }
              />
              <input
                type="number"
                placeholder="Ποσότητα"
                className="w-24 border p-2 rounded"
                value={tempIngredient.quantity}
                onChange={(e) =>
                  setTempIngredient({
                    ...tempIngredient,
                    quantity: e.target.value,
                  })
                }
              />
              <select
                className="w-32 border p-2 rounded bg-white"
                value={tempIngredient.unit}
                onChange={(e) =>
                  setTempIngredient({ ...tempIngredient, unit: e.target.value })
                }
              >
                {unitsList.map((u, i) => (
                  <option key={i} value={u}>
                    {u}
                  </option>
                ))}
              </select>
              <button
                type="button"
                onClick={addIngredient}
                className="bg-green-500 hover:bg-green-600 text-white font-bold px-4 rounded transition"
              >
                +
              </button>
            </div>

            <div className="flex flex-wrap gap-2">
              {recipe.ingredients.length === 0 && (
                <span className="text-gray-400 italic">
                  Δεν έχουν προστεθεί υλικά.
                </span>
              )}
              {recipe.ingredients.map((ing, idx) => (
                <div
                  key={idx}
                  className="bg-blue-50 text-blue-800 border border-blue-200 px-3 py-1 rounded-full flex items-center gap-2"
                >
                  <span>
                    {ing.name}{" "}
                    <span className="font-bold">
                      {ing.quantity}
                      {ing.unit}
                    </span>
                  </span>
                  <button
                    type="button"
                    onClick={() => removeIngredient(idx)}
                    className="text-red-500 hover:text-red-700 font-bold"
                  >
                    ×
                  </button>
                </div>
              ))}
            </div>
          </div>

          <hr className="border-gray-200" />

          {/* 3. Βήματα Εκτέλεσης */}
          <div>
            <h2 className="text-xl font-bold text-gray-800 mb-4">
              👣 Βήματα Εκτέλεσης
            </h2>

            {/* Φόρμα Βήματος */}
            <div className="bg-gray-50 p-5 rounded border border-gray-200 mb-6">
              <div className="grid grid-cols-3 gap-4 mb-3">
                <div className="col-span-2">
                  <input
                    type="text"
                    placeholder="Τίτλος Βήματος (π.χ. Προετοιμασία Σάλτσας)"
                    className="w-full border p-2 rounded"
                    value={tempStep.title}
                    onChange={(e) =>
                      setTempStep({ ...tempStep, title: e.target.value })
                    }
                  />
                </div>
                <div>
                  <input
                    type="number"
                    placeholder="Διάρκεια (λεπτά)"
                    className="w-full border p-2 rounded"
                    value={tempStep.duration}
                    onChange={(e) =>
                      setTempStep({ ...tempStep, duration: e.target.value })
                    }
                  />
                </div>
              </div>

              <textarea
                placeholder="Περιγραφή βήματος..."
                className="w-full border p-2 rounded mb-3 h-20"
                value={tempStep.description}
                onChange={(e) =>
                  setTempStep({ ...tempStep, description: e.target.value })
                }
              ></textarea>

              <input
                type="text"
                placeholder="URL Φωτογραφίας (προαιρετικό)"
                className="w-full border p-2 rounded mb-3"
                value={tempStep.photoUrl}
                onChange={(e) =>
                  setTempStep({ ...tempStep, photoUrl: e.target.value })
                }
              />

              {/* Checkboxes Υλικών */}
              <div className="bg-white p-3 rounded border mb-3">
                <p className="text-sm font-bold text-gray-600 mb-2">
                  Ποια υλικά χρησιμοποιούνται σε αυτό το βήμα;
                </p>
                {recipe.ingredients.length === 0 ? (
                  <p className="text-xs text-red-400">
                    Πρόσθεσε πρώτα υλικά παραπάνω!
                  </p>
                ) : (
                  <div className="flex flex-wrap gap-3">
                    {recipe.ingredients.map((ing, idx) => (
                      <label
                        key={idx}
                        className="flex items-center gap-2 cursor-pointer bg-gray-50 px-2 py-1 rounded hover:bg-gray-100 border"
                      >
                        <input
                          type="checkbox"
                          className="w-4 h-4 accent-blue-600"
                          checked={tempStep.selectedIngredients.includes(
                            ing.name
                          )}
                          onChange={() => toggleStepIngredient(ing.name)}
                        />
                        <span className="text-sm">{ing.name}</span>
                      </label>
                    ))}
                  </div>
                )}
              </div>

              <button
                type="button"
                onClick={addStep}
                className="w-full bg-purple-600 hover:bg-purple-700 text-white font-bold py-2 rounded transition"
              >
                Προσθήκη Βήματος
              </button>
            </div>

            {/* Λίστα Βημάτων */}
            <div className="space-y-4">
              {recipe.steps.length === 0 && (
                <p className="text-gray-400 italic text-center">
                  Δεν έχουν προστεθεί βήματα.
                </p>
              )}
              {recipe.steps.map((step, idx) => (
                <div
                  key={idx}
                  className="bg-white border rounded-lg shadow-sm overflow-hidden flex"
                >
                  <div className="bg-purple-100 w-12 flex items-center justify-center font-bold text-purple-800 text-xl">
                    {step.stepOrder}
                  </div>
                  <div className="p-4 flex-1">
                    <div className="flex justify-between items-start">
                      <h3 className="font-bold text-lg">{step.title}</h3>
                      <span className="text-xs bg-gray-200 px-2 py-1 rounded">
                        ⏱️ {step.duration}'
                      </span>
                    </div>
                    <p className="text-gray-600 text-sm mt-1">
                      {step.description}
                    </p>

                    {step.selectedIngredients.length > 0 && (
                      <div className="mt-2 flex flex-wrap gap-1">
                        {step.selectedIngredients.map((ing, i) => (
                          <span
                            key={i}
                            className="text-xs bg-yellow-100 text-yellow-800 px-1 rounded border border-yellow-200"
                          >
                            {ing}
                          </span>
                        ))}
                      </div>
                    )}
                  </div>
                  <button
                    type="button"
                    onClick={() => removeStep(idx)}
                    className="bg-red-50 hover:bg-red-100 text-red-500 w-12 flex items-center justify-center border-l transition"
                  >
                    🗑️
                  </button>
                </div>
              ))}
            </div>
          </div>

          {/* Κουμπί Save */}
          <div className="pt-6">
            <button
              type="submit"
              className="w-full bg-green-600 hover:bg-green-700 text-white font-bold py-4 rounded-lg text-xl shadow-lg transform active:scale-95 transition"
            >
              💾 Αποθήκευση Συνταγής
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
