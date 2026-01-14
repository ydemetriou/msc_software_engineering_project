"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";
import API_BASE_URL from "@/config/api";

export default function EditRecipe({ params: paramsPromise }) {
  const router = useRouter();

  // Unwrap params
  const [id, setId] = useState(null);
  useEffect(() => {
    if (paramsPromise instanceof Promise) {
      paramsPromise.then((p) => setId(p.id));
    } else {
      setId(paramsPromise.id);
    }
  }, [paramsPromise]);

  const [loading, setLoading] = useState(true);

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
    mainPhotoUrl: "",
    ingredients: [],
    steps: [],
  });

  const [tempIngredient, setTempIngredient] = useState({
    name: "",
    quantity: "",
    unit: "",
  });

  const [tempStep, setTempStep] = useState({
    title: "",
    description: "",
    duration: "",
    photoUrls: [], // Πίνακας για πολλαπλές
    selectedIngredients: [],
  });
  const [editingStepIndex, setEditingStepIndex] = useState(null);

  // ---  Upload Αρχείων (Multiple) ---
  const handleFileUpload = (e, targetField, isStep = false) => {
    const files = Array.from(e.target.files);
    if (!files.length) return;

    // Έλεγχος μεγέθους
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
          // Προσθέτουμε τις νέες στις υπάρχουσες
          [targetField]: [...(prev[targetField] || []), ...base64Array],
        }));
      } else {
        setRecipe((prev) => ({ ...prev, [targetField]: base64Array[0] }));
      }
    });
  };
  // ---------------------------------------------

  // 1. Fetch Data
  useEffect(() => {
    if (!id) return;

    const loadData = async () => {
      try {
        const [catRes, unitRes, diffRes, recipeRes] = await Promise.all([
          fetch(`${API_BASE_URL}/api/references/categories`),
          fetch(`${API_BASE_URL}/api/references/units`),
          fetch(`${API_BASE_URL}/api/references/difficulties`),
          fetch(`${API_BASE_URL}/api/recipes/${id}`),
        ]);

        const catData = await catRes.json();
        const unitData = await unitRes.json();
        const diffData = await diffRes.json();
        const recipeData = await recipeRes.json();

        setCategoriesList(catData);
        setUnitsList(unitData);
        setDifficultiesList(diffData);

        // Populate Form with existing data
        setRecipe({
          name: recipeData.name || "",
          category: recipeData.category || catData[0] || "",
          difficulty: recipeData.difficulty || diffData[0] || "",
          totalTime: recipeData.totalTime || 0,
          mainPhotoUrl: recipeData.photoUrls?.[0] || "",

          ingredients: recipeData.ingredients.map((i) => ({
            name: i.name,
            quantity: i.quantity,
            unit: i.unit || unitData[0],
          })),

          steps: recipeData.steps.map((s) => ({
            title: s.title,
            description: s.description,
            duration: s.duration,
            photoUrls: s.photoUrls || [], // Φόρτωση λίστας φωτογραφιών
            selectedIngredients: s.ingredients
              ? s.ingredients.map((i) => i.name)
              : [],
            stepOrder: s.stepOrder,
          })),
        });

        setTempIngredient((prev) => ({ ...prev, unit: unitData[0] || "GR" }));
        setLoading(false);
      } catch (err) {
        console.error("Error:", err);
        alert("Σφάλμα φόρτωσης δεδομένων");
        router.push("/");
      }
    };

    loadData();
  }, [id, router]);

  // Auto-calculate time
  useEffect(() => {
    const total = recipe.steps.reduce(
      (sum, step) => sum + parseInt(step.duration || 0),
      0
    );
    setRecipe((prev) => ({ ...prev, totalTime: total }));
  }, [recipe.steps]);

  // Submit Handler
  const handleSubmit = async (e) => {
    e.preventDefault();

    const calculatedTime = recipe.steps.reduce(
      (sum, step) => sum + parseInt(step.duration || 0),
      0
    );

    // Payload
    const payload = {
      name: recipe.name,
      category: recipe.category,
      difficulty: recipe.difficulty,
      totalTime: calculatedTime,
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
          photoUrls: step.photoUrls || [], // Αποστολή πίνακα
          ingredients: stepIngredientsObjects,
        };
      }),
    };

    try {
      const res = await fetch(`${API_BASE_URL}/api/recipes/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });

      if (res.ok) {
        alert("Η συνταγή ενημερώθηκε!");
        router.push(`/recipe/${id}`);
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

    setRecipe((prev) => {
      let updatedSteps = [...prev.steps];

      if (editingStepIndex !== null) {
        // --- LOGIC ΓΙΑ UPDATE ΥΠΑΡΧΟΝΤΟΣ ---
        updatedSteps[editingStepIndex] = {
          ...tempStep,
          stepOrder: editingStepIndex + 1, // Κρατάμε τη σωστή σειρά
        };
      } else {
        // --- LOGIC ΓΙΑ ΠΡΟΣΘΗΚΗ ΝΕΟΥ ---
        updatedSteps.push({
          ...tempStep,
          stepOrder: updatedSteps.length + 1,
        });
      }

      return { ...prev, steps: updatedSteps };
    });

    // Καθαρισμός φόρμας και reset του index
    setTempStep({
      title: "",
      description: "",
      duration: "",
      photoUrls: [], // Reset array
      selectedIngredients: [],
    });
    setEditingStepIndex(null); // Επιστροφή σε λειτουργία "Προσθήκης"
  };

  const editStep = (idx) => {
    const stepToEdit = recipe.steps[idx];
    // Φορτώνουμε τα δεδομένα στη φόρμα
    setTempStep({
      title: stepToEdit.title,
      description: stepToEdit.description,
      duration: stepToEdit.duration,
      photoUrls: stepToEdit.photoUrls || [], // Φόρτωση εικόνων
      selectedIngredients: stepToEdit.selectedIngredients || [],
    });
    // Σημειώνουμε ποιο index επεξεργαζόμαστε
    setEditingStepIndex(idx);
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

  if (loading) return <div className="p-10 text-center">Φόρτωση...</div>;

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-4xl mx-auto bg-white shadow rounded-xl overflow-hidden">
        <div className="bg-orange-600 p-6 flex justify-between items-center text-white">
          <h1 className="text-3xl font-bold">✏️ Επεξεργασία Συνταγής</h1>
          <Link href={`/recipe/${id}`} className="hover:underline">
            ← Ακύρωση
          </Link>
        </div>

        <form onSubmit={handleSubmit} className="p-8 space-y-8">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className="block font-bold mb-1">Όνομα</label>
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
              {/* Preview */}
              {recipe.mainPhotoUrl && (
                <div className="mt-2 w-20 h-20 border rounded overflow-hidden relative group">
                  <img
                    src={recipe.mainPhotoUrl}
                    alt="Preview"
                    className="w-full h-full object-cover"
                  />
                  {/* Κουμπί αφαίρεσης φωτο αν θέλουμε (προαιρετικό) */}
                  <button
                    type="button"
                    className="absolute top-0 right-0 bg-red-600 text-white text-xs p-1 opacity-0 group-hover:opacity-100 transition"
                    onClick={() => setRecipe({ ...recipe, mainPhotoUrl: "" })}
                  >
                    X
                  </button>
                </div>
              )}
            </div>
            {/* ----------------------------------------------- */}

            <div>
              <label className="block font-bold mb-1">Κατηγορία</label>
              <select
                className="w-full border p-2 rounded"
                value={recipe.category || ""}
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
                value={recipe.difficulty || ""}
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
              <label className="block font-bold mb-1">Συνολικός Χρόνος</label>
              <input
                type="number"
                className="w-32 border p-2 rounded bg-gray-100 font-bold"
                readOnly
                value={recipe.totalTime}
              />
            </div>
          </div>

          <hr />

          {/* Ingredients */}
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
                value={tempIngredient.unit || ""}
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
                className="bg-orange-500 text-white px-4 rounded"
              >
                +
              </button>
            </div>
            <div className="flex flex-wrap gap-2">
              {recipe.ingredients.map((ing, i) => (
                <div
                  key={i}
                  className="bg-orange-50 text-orange-800 px-3 py-1 rounded-full border border-orange-200 flex items-center gap-2"
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

          {/* Steps */}
          <div>
            <h2 className="text-xl font-bold mb-4">👣 Βήματα</h2>
            <div className="bg-gray-50 p-4 rounded border mb-6">
              <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-3">
                <div className="md:col-span-2">
                  <input
                    type="text"
                    placeholder="Τίτλος"
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
                placeholder="Περιγραφή"
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
                  multiple // Πολλαπλή επιλογή
                  accept="image/*"
                  className="w-full border p-2 rounded bg-white"
                  onChange={(e) => handleFileUpload(e, "photoUrls", true)}
                />

                {/* Προεπισκόπηση */}
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
                <span className="font-bold text-sm block mb-2">
                  Υλικά Βήματος:
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
              {/* Κουμπιά Action για τα Βήματα */}
              <div className="flex gap-2">
                <button
                  type="button"
                  onClick={addStep}
                  className={`flex-1 font-bold py-2 rounded text-white ${
                    editingStepIndex !== null
                      ? "bg-blue-600 hover:bg-blue-700"
                      : "bg-orange-600 hover:bg-orange-700"
                  }`}
                >
                  {editingStepIndex !== null
                    ? "💾 Ενημέρωση Βήματος"
                    : "➕ Προσθήκη Βήματος"}
                </button>

                {/* Κουμπί Ακύρωσης */}
                {editingStepIndex !== null && (
                  <button
                    type="button"
                    onClick={() => {
                      setEditingStepIndex(null);
                      setTempStep({
                        title: "",
                        description: "",
                        duration: "",
                        photoUrls: [],
                        selectedIngredients: [],
                      });
                    }}
                    className="bg-gray-400 text-white px-4 rounded hover:bg-gray-500 font-bold"
                  >
                    Ακύρωση
                  </button>
                )}
              </div>
            </div>

            <div className="space-y-4">
              {recipe.steps.map((step, i) => (
                <div
                  key={i}
                  className="border rounded flex overflow-hidden bg-white shadow-sm"
                >
                  <div className="bg-orange-100 w-10 flex items-center justify-center font-bold text-orange-700">
                    {i + 1}
                  </div>

                  {step.photoUrls && step.photoUrls.length > 0 && (
                    <div className="w-24 h-24 flex-shrink-0 bg-gray-200">
                      <img
                        src={step.photoUrls[0]}
                        alt="step"
                        className="w-full h-full object-cover"
                      />
                    </div>
                  )}

                  <div className="p-3 flex-1">
                    <div className="flex justify-between">
                      <h4 className="font-bold">{step.title}</h4>
                      <span className="text-xs bg-gray-200 px-2 py-1 rounded">
                        {step.duration}'
                      </span>
                    </div>
                    <p className="text-sm text-gray-600 mt-1">
                      {step.description}
                    </p>
                  </div>

                  {/* Κουμπιά Edit & Delete */}
                  <div className="flex flex-col border-l">
                    <button
                      type="button"
                      onClick={() => editStep(i)}
                      className="px-3 py-2 text-blue-500 hover:bg-blue-50 border-b"
                    >
                      ✏️
                    </button>
                    <button
                      type="button"
                      onClick={() => removeStep(i)}
                      className="px-3 py-2 text-red-500 hover:bg-red-50"
                    >
                      ×
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>

          <button
            type="submit"
            className="w-full bg-orange-600 text-white font-bold py-3 rounded text-lg shadow hover:bg-orange-700"
          >
            💾 Αποθήκευση Αλλαγών
          </button>
        </form>
      </div>
    </div>
  );
}
