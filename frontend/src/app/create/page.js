"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import API_BASE_URL from "@/config/api";

export default function CreateRecipe() {
  const router = useRouter();
  const [loadingLists, setLoadingLists] = useState(true);

  // Lists
  const [categoriesList, setCategoriesList] = useState([]);
  const [unitsList, setUnitsList] = useState([]);
  const [difficultiesList, setDifficultiesList] = useState([]);

  // Form State
  const [recipe, setRecipe] = useState({
    name: "",
    category: "",
    difficulty: "",
    totalTime: 0,
    mainPhotoUrl: "", // Κεντρική (μία)
    ingredients: [],
    steps: [],
  });

  // Temporary Inputs
  const [tempIngredient, setTempIngredient] = useState({
    name: "",
    quantity: "",
    unit: "",
  });

  const [tempStep, setTempStep] = useState({
    title: "",
    description: "",
    duration: "",
    photoUrls: [], // Πίνακας για πολλαπλές φωτό
    selectedIngredients: [],
  });

  // --- Συνάρτηση μετατροπής αρχείων σε Base64 (Multiple) ---
  const handleFileUpload = (e, targetField, isStep = false) => {
    const files = Array.from(e.target.files);
    if (!files.length) return;

    // Έλεγχος μεγέθους για κάθε αρχείο
    for (let file of files) {
      if (file.size > 2 * 1024 * 1024) {
        alert(`Το αρχείο ${file.name} είναι πολύ μεγάλο! (>2MB)`);
        return;
      }
    }

    const readers = files.map((file) => {
      return new Promise((resolve) => {
        const reader = new FileReader();
        reader.onloadend = () => resolve(reader.result);
        reader.readAsDataURL(file);
      });
    });

    Promise.all(readers).then((base64Array) => {
      if (isStep) {
        setTempStep((prev) => ({
          ...prev,
          // Προσθήκη των νέων στις υπάρχουσες
          [targetField]: [...(prev[targetField] || []), ...base64Array],
        }));
      } else {
        // Για την κεντρική κρατάμε μόνο την πρώτη (όπως πριν)
        setRecipe((prev) => ({ ...prev, [targetField]: base64Array[0] }));
      }
    });
  };

  // 1. Fetch Lists & Initial Setup
  useEffect(() => {
    const fetchData = async () => {
      try {
        const [catRes, unitRes, diffRes] = await Promise.all([
          fetch(`${API_BASE_URL}/api/references/categories`),
          fetch(`${API_BASE_URL}/api/references/units`),
          fetch(`${API_BASE_URL}/api/references/difficulties`),
        ]);

        const catData = await catRes.json();
        const unitData = await unitRes.json();
        const diffData = await diffRes.json();

        setCategoriesList(catData);
        setUnitsList(unitData);
        setDifficultiesList(diffData);

        // Defaults
        setRecipe((prev) => ({
          ...prev,
          category: catData[0] || "Ζυμαρικά",
          difficulty: diffData[0] || "EASY",
        }));
        setTempIngredient((prev) => ({ ...prev, unit: unitData[0] || "GR" }));
        setLoadingLists(false);
      } catch (err) {
        console.error("Error loading lists:", err);
        setLoadingLists(false);
      }
    };
    fetchData();
  }, []);

  // 2. Auto Calc Time
  useEffect(() => {
    const total = recipe.steps.reduce(
      (sum, step) => sum + parseInt(step.duration || 0),
      0
    );
    setRecipe((prev) => ({ ...prev, totalTime: total }));
  }, [recipe.steps]);

  // 3. Submit Handler
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (recipe.ingredients.length === 0 || recipe.steps.length === 0) {
      alert("Πρόσθεσε υλικά και βήματα!");
      return;
    }

    const payload = {
      name: recipe.name,
      category: recipe.category,
      difficulty: recipe.difficulty,
      totalTime: recipe.totalTime,
      photoUrls: recipe.mainPhotoUrl ? [recipe.mainPhotoUrl] : [],
      ingredients: recipe.ingredients.map((ing) => ({
        name: ing.name,
        quantity: parseFloat(ing.quantity),
        unit: ing.unit,
      })),
      steps: recipe.steps.map((step) => {
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
          photoUrls: step.photoUrls || [], // Στέλνουμε τον πίνακα
          ingredients: stepIngredientsObjects,
        };
      }),
    };

    try {
      const res = await fetch(`${API_BASE_URL}/api/recipes`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (res.ok) {
        alert("Επιτυχία!");
        router.push("/");
      } else {
        const errorData = await res.json();
        alert("Σφάλμα: " + (errorData.message || "Κάτι πήγε στραβά"));
      }
    } catch (err) {
      alert("Σφάλμα σύνδεσης");
    }
  };

  // Helper Functions
  const addIngredient = () => {
    if (!tempIngredient.name || !tempIngredient.quantity) return;
    setRecipe((prev) => ({
      ...prev,
      ingredients: [...prev.ingredients, tempIngredient],
    }));
    setTempIngredient((prev) => ({ ...prev, name: "", quantity: "" }));
  };

  const removeIngredient = (idx) => {
    const ingName = recipe.ingredients[idx].name;
    setRecipe((prev) => ({
      ...prev,
      ingredients: prev.ingredients.filter((_, i) => i !== idx),
      steps: prev.steps.map((s) => ({
        ...s,
        selectedIngredients: s.selectedIngredients.filter((n) => n !== ingName),
      })),
    }));
  };

  const addStep = () => {
    if (!tempStep.title || !tempStep.duration) {
      alert("Συμπλήρωσε τίτλο και διάρκεια!");
      return;
    }
    setRecipe((prev) => ({
      ...prev,
      steps: [...prev.steps, { ...tempStep, stepOrder: prev.steps.length + 1 }],
    }));
    setTempStep({
      title: "",
      description: "",
      duration: "",
      photoUrls: [], // Reset array
      selectedIngredients: [],
    });
  };

  const removeStep = (idx) => {
    setRecipe((prev) => ({
      ...prev,
      steps: prev.steps
        .filter((_, i) => i !== idx)
        .map((s, i) => ({ ...s, stepOrder: i + 1 })),
    }));
  };

  const toggleStepIngredient = (name) => {
    setTempStep((prev) => {
      const list = prev.selectedIngredients.includes(name)
        ? prev.selectedIngredients.filter((n) => n !== name)
        : [...prev.selectedIngredients, name];
      return { ...prev, selectedIngredients: list };
    });
  };

  if (loadingLists) return <div className="p-10">Φόρτωση...</div>;

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-4xl mx-auto bg-white shadow rounded-xl overflow-hidden">
        <div className="bg-blue-600 p-6 flex justify-between items-center text-white">
          <h1 className="text-3xl font-bold">🍳 Νέα Συνταγή</h1>
          <Link href="/" className="hover:underline">
            ← Πίσω
          </Link>
        </div>

        <form onSubmit={handleSubmit} className="p-8 space-y-8">
          {/* ΓΕΝΙΚΑ ΣΤΟΙΧΕΙΑ */}
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block font-bold mb-1">Όνομα Συνταγής</label>
              <input
                type="text"
                className="w-full border p-2 rounded"
                required
                value={recipe.name}
                onChange={(e) => setRecipe({ ...recipe, name: e.target.value })}
              />
            </div>

            {/* --- File Input για Κεντρική Φωτογραφία --- */}
            <div>
              <label className="block font-bold mb-1">
                Κεντρική Φωτογραφία
              </label>
              <input
                type="file"
                accept="image/*"
                className="w-full border p-2 rounded bg-gray-50"
                onChange={(e) => handleFileUpload(e, "mainPhotoUrl", false)}
              />
              {/* Προεπισκόπηση */}
              {recipe.mainPhotoUrl && (
                <div className="mt-2 w-20 h-20 border rounded overflow-hidden">
                  <img
                    src={recipe.mainPhotoUrl}
                    alt="Preview"
                    className="w-full h-full object-cover"
                  />
                </div>
              )}
            </div>
            {/* ----------------------------------------------- */}

            <div>
              <label className="block font-bold mb-1">Κατηγορία</label>
              <select
                className="w-full border p-2 rounded"
                value={recipe.category}
                onChange={(e) =>
                  setRecipe({ ...recipe, category: e.target.value })
                }
              >
                {categoriesList.map((c) => (
                  <option key={c} value={c}>
                    {c}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block font-bold mb-1">Δυσκολία</label>
              <select
                className="w-full border p-2 rounded"
                value={recipe.difficulty}
                onChange={(e) =>
                  setRecipe({ ...recipe, difficulty: e.target.value })
                }
              >
                {difficultiesList.map((d) => (
                  <option key={d} value={d}>
                    {d}
                  </option>
                ))}
              </select>
            </div>

            <div className="md:col-span-2">
              <label className="block font-bold mb-1">
                Συνολικός Χρόνος (Αυτόματος)
              </label>
              <div className="flex items-center gap-2">
                <input
                  type="number"
                  className="w-32 border p-2 rounded bg-gray-100 font-bold text-blue-600"
                  readOnly
                  value={recipe.totalTime}
                />
                <span className="text-gray-500 text-sm">
                  (Υπολογίζεται από το άθροισμα των βημάτων)
                </span>
              </div>
            </div>
          </div>

          <hr />

          {/* ΥΛΙΚΑ (ΙΔΙΟ ΚΩΔΙΚΑΣ) */}
          <div>
            <h2 className="text-xl font-bold mb-4">🛒 Υλικά</h2>
            <div className="flex gap-2 mb-4">
              <input
                type="text"
                placeholder="Όνομα"
                className="border p-2 rounded flex-1"
                value={tempIngredient.name}
                onChange={(e) =>
                  setTempIngredient({ ...tempIngredient, name: e.target.value })
                }
              />
              <input
                type="number"
                placeholder="Ποσότητα"
                className="border p-2 rounded w-24"
                value={tempIngredient.quantity}
                onChange={(e) =>
                  setTempIngredient({
                    ...tempIngredient,
                    quantity: e.target.value,
                  })
                }
              />
              <select
                className="border p-2 rounded w-32"
                value={tempIngredient.unit}
                onChange={(e) =>
                  setTempIngredient({ ...tempIngredient, unit: e.target.value })
                }
              >
                {unitsList.map((u) => (
                  <option key={u} value={u}>
                    {u}
                  </option>
                ))}
              </select>
              <button
                type="button"
                onClick={addIngredient}
                className="bg-green-500 text-white px-4 rounded"
              >
                +
              </button>
            </div>
            <div className="flex flex-wrap gap-2">
              {recipe.ingredients.map((ing, i) => (
                <div
                  key={i}
                  className="bg-blue-50 text-blue-800 px-3 py-1 rounded-full border border-blue-200 flex items-center gap-2"
                >
                  {ing.name}{" "}
                  <b>
                    {ing.quantity}
                    {ing.unit}
                  </b>
                  <button
                    type="button"
                    onClick={() => removeIngredient(i)}
                    className="text-red-500 font-bold"
                  >
                    ×
                  </button>
                </div>
              ))}
            </div>
          </div>

          <hr />

          {/* ΒΗΜΑΤΑ */}
          <div>
            <h2 className="text-xl font-bold mb-4">👣 Βήματα</h2>
            <div className="bg-gray-50 p-4 rounded border mb-6">
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-3">
                <div className="md:col-span-2">
                  <input
                    type="text"
                    placeholder="Τίτλος Βήματος"
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
                    placeholder="Λεπτά"
                    className="w-full border p-2 rounded"
                    value={tempStep.duration}
                    onChange={(e) =>
                      setTempStep({ ...tempStep, duration: e.target.value })
                    }
                  />
                </div>
              </div>
              <textarea
                placeholder="Περιγραφή..."
                className="w-full border p-2 rounded mb-3 h-20"
                value={tempStep.description}
                onChange={(e) =>
                  setTempStep({ ...tempStep, description: e.target.value })
                }
              />

              {/* --- File Input για Φωτογραφίες Βήματος (Multiple) --- */}
              <div className="mb-3">
                <label className="block text-sm font-bold text-gray-600 mb-1">
                  Φωτογραφίες Βήματος (Πολλαπλές)
                </label>
                <input
                  type="file"
                  multiple // Επιτρέπει πολλαπλή επιλογή
                  accept="image/*"
                  className="w-full border p-2 rounded bg-white"
                  // Στέλνουμε "photoUrls" ως targetField
                  onChange={(e) => handleFileUpload(e, "photoUrls", true)}
                />

                {/* Προεπισκόπηση Πολλαπλών Εικόνων */}
                <div className="flex gap-2 mt-2 flex-wrap">
                  {tempStep.photoUrls &&
                    tempStep.photoUrls.map((url, idx) => (
                      <div
                        key={idx}
                        className="relative w-16 h-16 border rounded overflow-hidden group"
                      >
                        <img
                          src={url}
                          alt={`Step Preview ${idx}`}
                          className="w-full h-full object-cover"
                        />
                        {/* Κουμπάκι για αφαίρεση συγκεκριμένης εικόνας */}
                        <button
                          type="button"
                          onClick={() => {
                            setTempStep((prev) => ({
                              ...prev,
                              photoUrls: prev.photoUrls.filter(
                                (_, i) => i !== idx
                              ),
                            }));
                          }}
                          className="absolute top-0 right-0 bg-red-600 text-white text-xs w-4 h-4 flex items-center justify-center opacity-0 group-hover:opacity-100"
                        >
                          ×
                        </button>
                      </div>
                    ))}
                </div>
              </div>
              {/* ------------------------------------------------ */}

              <div className="mb-3">
                <span className="font-bold text-sm text-gray-600 block mb-2">
                  Υλικά σε αυτό το βήμα:
                </span>
                <div className="flex flex-wrap gap-2">
                  {recipe.ingredients.map((ing, i) => (
                    <label
                      key={i}
                      className="flex items-center gap-1 bg-white border px-2 py-1 rounded cursor-pointer"
                    >
                      <input
                        type="checkbox"
                        checked={tempStep.selectedIngredients.includes(
                          ing.name
                        )}
                        onChange={() => toggleStepIngredient(ing.name)}
                      />
                      <span className="text-sm">{ing.name}</span>
                    </label>
                  ))}
                </div>
              </div>

              <button
                type="button"
                onClick={addStep}
                className="w-full bg-purple-600 text-white font-bold py-2 rounded"
              >
                Προσθήκη Βήματος
              </button>
            </div>

            <div className="space-y-4">
              {recipe.steps.map((step, i) => (
                <div
                  key={i}
                  className="border rounded flex overflow-hidden bg-white shadow-sm"
                >
                  <div className="bg-purple-100 w-10 flex items-center justify-center font-bold text-purple-700">
                    {step.stepOrder}
                  </div>

                  {step.photoUrls && step.photoUrls.length > 0 && (
                    <div className="w-24 h-24 flex-shrink-0 bg-gray-200">
                      <img
                        src={step.photoUrls[0]} // Δείχνουμε την πρώτη στη λίστα
                        alt="Step"
                        className="w-full h-full object-cover"
                      />
                    </div>
                  )}

                  <div className="p-3 flex-1">
                    <div className="flex justify-between">
                      <h4 className="font-bold">{step.title}</h4>
                      <span className="text-xs bg-gray-200 px-2 py-1 rounded">
                        {step.duration} λεπτά
                      </span>
                    </div>
                    <p className="text-sm text-gray-600 mt-1">
                      {step.description}
                    </p>
                    <div className="mt-2 text-xs text-gray-500">
                      {step.selectedIngredients.length > 0 &&
                        "Υλικά: " + step.selectedIngredients.join(", ")}
                    </div>
                  </div>
                  <button
                    type="button"
                    onClick={() => removeStep(i)}
                    className="px-4 text-red-500 font-bold hover:bg-red-50"
                  >
                    ×
                  </button>
                </div>
              ))}
            </div>
          </div>

          <button
            type="submit"
            className="w-full bg-green-600 text-white font-bold py-3 rounded text-lg shadow hover:bg-green-700"
          >
            💾 Αποθήκευση Συνταγής ({recipe.totalTime} λεπτά)
          </button>
        </form>
      </div>
    </div>
  );
}
